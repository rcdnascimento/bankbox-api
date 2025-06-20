package com.bankbox.domain.service.creditcardtransaction.impl;

import com.bankbox.domain.entity.*;
import com.bankbox.domain.service.creditcard.impl.CreditCardService;
import com.bankbox.domain.service.creditcardinvoice.CreditCardInvoiceService;
import com.bankbox.domain.service.creditcardtransaction.PersistCreditCardTransaction;
import com.bankbox.domain.service.creditcardtransaction.RetrieveCreditCardTransaction;
import com.bankbox.infra.dto.CreditCardTransactionRequest;
import com.bankbox.infra.repository.CreditCardInstallmentRepository;
import com.bankbox.infra.repository.CreditCardTransactionRepository;
import com.bankbox.infra.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditCardTransactionService implements RetrieveCreditCardTransaction, PersistCreditCardTransaction {

  private final CreditCardTransactionRepository creditCardTransactionRepository;
  private final CreditCardInstallmentRepository creditCardInstallmentRepository;
  private final CreditCardInvoiceService creditCardInvoiceService;
  private final CreditCardService creditCardService;

  @Override
  public List<CreditCardTransaction> retrieveByCreditCard(Long creditCardId) {
    return creditCardTransactionRepository.findAllByCreditCardId(creditCardId);
  }

  @Override
  public CreditCardTransaction createTransaction(CreditCardTransactionRequest request) {
//    CreditCard creditCard = creditCardService.retrieveById(request.creditCardId);
    CreditCard creditCard = new CreditCard(request.creditCardId);

    YearMonth month = YearMonth.from(request.processedAt);

    Optional<CreditCardInvoice> transactionInvoice = creditCardInvoiceService.findByCreditCardAndMonth(creditCard.getId(), month);

    CreditCardTransaction transaction = new CreditCardTransaction();
    transaction.setProcessedAt(request.processedAt);
    transaction.setCategory(request.category);
    transaction.setValue(request.value);
    transaction.setMerchantName(request.merchantName);
    transaction.setCreditCard(creditCard);

    if (transactionInvoice.isEmpty()) {
      LocalDateTime creditCardDueDate = getCreditCardInvoicesDueDateForDate(request.processedAt, creditCard.getId());

      CreditCardInvoice newInvoice = new CreditCardInvoice();
      newInvoice.setCreditCard(creditCard);
      newInvoice.setMonth(month);
      newInvoice.setDueDate(creditCardDueDate);

      transaction.setInvoice(creditCardInvoiceService.createInvoice(newInvoice));
    } else {
      transaction.setInvoice(transactionInvoice.get());
    }

    CreditCardTransaction createdTransaction = creditCardTransactionRepository.save(transaction);

    if (request.installments > 1) {
      return createInstallmentsForTransaction(createdTransaction, request.installments);
    }

    return createdTransaction;
  }

  private CreditCardTransaction createInstallmentsForTransaction(CreditCardTransaction transaction, int installmentsNumber) {
    List<CreditCardInstallment> installments = new ArrayList<>();
    BigDecimal installmentValue = transaction.getValue().divide(BigDecimal.valueOf(installmentsNumber), RoundingMode.CEILING);

    for (int installmentNumber = 1; installmentNumber <= installmentsNumber; installmentNumber++) {
      CreditCardInstallment creditCardInstallment = new CreditCardInstallment();
      creditCardInstallment.setTransaction(transaction);
      creditCardInstallment.setValue(installmentValue);
      creditCardInstallment.setStatus(InstallmentStatusEnum.PENDING);
      creditCardInstallment.setNumber(installmentNumber);
      installments.add(creditCardInstallment);
    }

    transaction.setInstallments(creditCardInstallmentRepository.saveAll(installments));
    return transaction;
  }

  private LocalDateTime getCreditCardInvoicesDueDateForDate(LocalDateTime dateTime, Long creditCardId) {
    Optional<CreditCardInvoice> lastCreditCardInvoice = creditCardInvoiceService.findLastCreditCardInvoice(creditCardId);

    if (lastCreditCardInvoice.isEmpty()) {
      return dateTime.plusMonths(1).withDayOfMonth(5);
    }

    return lastCreditCardInvoice.get().getDueDate();
  }
}

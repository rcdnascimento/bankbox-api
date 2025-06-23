package com.bankbox.domain.service.creditcardinvoice.impl;

import com.bankbox.domain.entity.CreditCardInvoice;
import com.bankbox.domain.service.creditcardinvoice.CreditCardInvoiceService;
import com.bankbox.infra.repository.CreditCardInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditCardInvoiceImpl implements CreditCardInvoiceService {

  private final CreditCardInvoiceRepository creditCardInvoiceRepository;

  public Optional<CreditCardInvoice> findLastCreditCardInvoice(Long creditCardId) {
    return creditCardInvoiceRepository.findFirstByCreditCardIdOrderByDueDateDesc(creditCardId);
  }

  @Override
  public Optional<CreditCardInvoice> findByCreditCardAndMonth(Long creditCardId, YearMonth month) {
    return creditCardInvoiceRepository.findByCreditCardIdAndMonth(creditCardId, month);
  }

  @Override
  public CreditCardInvoice createInvoice(CreditCardInvoice invoice) {
    return creditCardInvoiceRepository.save(invoice);
  }

  @Override
  public List<CreditCardInvoice> retrieveInvoicesByCreditCardAndMonth(Long creditCardId, YearMonth month) {
    if (Objects.nonNull(month)) {
      return creditCardInvoiceRepository.findAllByCreditCardIdAndMonth(creditCardId, month);
    }

    return creditCardInvoiceRepository.findAllByCreditCardId(creditCardId);
  }
}

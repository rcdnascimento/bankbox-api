package com.bankbox.domain.service.creditcardinvoice;

import com.bankbox.domain.entity.CreditCardInvoice;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface CreditCardInvoiceService {
  Optional<CreditCardInvoice> findByCreditCardAndMonth(Long creditCardId, YearMonth month);
  List<CreditCardInvoice> retrieveInvoicesByCreditCardAndMonth(Long creditCardId, YearMonth month);
  Optional<CreditCardInvoice> findLastCreditCardInvoice(Long creditCardId);
  CreditCardInvoice createInvoice(CreditCardInvoice invoice);
}

package com.bankbox.infra.repository;

import com.bankbox.domain.entity.CreditCard;
import com.bankbox.domain.entity.CreditCardInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.Optional;

public interface CreditCardInvoiceRepository extends JpaRepository<CreditCardInvoice, Long> {
  Optional<CreditCardInvoice> findByMonth(YearMonth month);
  Optional<CreditCardInvoice> findFirstByCreditCardIdOrderByDueDateDesc(Long creditCardId);
}

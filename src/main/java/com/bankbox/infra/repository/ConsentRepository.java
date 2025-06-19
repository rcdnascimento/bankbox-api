package com.bankbox.infra.repository;

import com.bankbox.domain.entity.Consent;
import com.bankbox.domain.entity.CreditCardInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface ConsentRepository extends JpaRepository<Consent, Long> {
}

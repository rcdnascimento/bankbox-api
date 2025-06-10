package com.bankbox.infra.repository;

import com.bankbox.domain.entity.CreditCardInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardInstallmentRepository extends JpaRepository<CreditCardInstallment, Long> {
}

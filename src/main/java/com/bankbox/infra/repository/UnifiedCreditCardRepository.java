package com.bankbox.infra.repository;

import com.bankbox.domain.entity.UnifiedCreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnifiedCreditCardRepository extends JpaRepository<UnifiedCreditCard, Long> {
  Optional<UnifiedCreditCard> findByCustomerId(Long customerId);
}

package com.bankbox.infra.repository;

import com.bankbox.domain.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	List<CreditCard> findByCustomerId(Long customerId);
}

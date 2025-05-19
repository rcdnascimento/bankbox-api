package com.bankbox.infra.repository;

import com.bankbox.domain.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	@Query(value = "SELECT * FROM credit_card WHERE customer_id = ?1", nativeQuery = true)
	List<CreditCard> findByCustomerId(Long customerId);
	@Query(value = "SELECT * FROM credit_card WHERE id = LAST_INSERT_ID()", nativeQuery = true)
	CreditCard retrieveLastCreated();

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO credit_card (owner_name, number, expiration, security_number, type, brand, `limit`, customer_id) " +
			"VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery = true)
	void insertCreditCard(
		String ownerName,
		String cardNumber,
		String expirationDate,
		Integer securityNumber,
		String type,
		String brand,
		BigDecimal limit,
		Long customerId
	);
}

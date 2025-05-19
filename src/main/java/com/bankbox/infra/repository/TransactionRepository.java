package com.bankbox.infra.repository;

import com.bankbox.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	@Query(value = "SELECT * FROM `transaction` WHERE beneficiary_id = ?1 OR source_id = ?1", nativeQuery = true)
	List<Transaction> findBySourceOrBeneficiary(Long id);
}

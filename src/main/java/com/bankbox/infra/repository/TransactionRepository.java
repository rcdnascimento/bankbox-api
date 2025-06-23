package com.bankbox.infra.repository;

import com.bankbox.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	@Query(value = "SELECT * FROM `transaction` INNER JOIN bank_account ON transaction.source_id = bank_account.id OR transaction.beneficiary_id = bank_account.id WHERE bank_account.owner_id = ?1", nativeQuery = true)
	List<Transaction> findBySourceOrBeneficiary(Long id);
}

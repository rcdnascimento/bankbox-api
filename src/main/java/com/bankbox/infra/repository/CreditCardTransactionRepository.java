package com.bankbox.infra.repository;

import com.bankbox.domain.entity.CreditCardTransaction;
import com.bankbox.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long> {
	List<CreditCardTransaction> findAllByCreditCardId(Long creditCardId);
}

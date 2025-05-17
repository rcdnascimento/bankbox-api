package com.bankbox.domain.service.transaction;

import com.bankbox.domain.entity.Transaction;

import java.util.List;

public interface RetrieveTransaction {
	List<Transaction> retrieveByCustomer(Long id);
}

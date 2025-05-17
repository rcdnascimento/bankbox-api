package com.bankbox.domain.service.transaction;

import com.bankbox.domain.entity.Transaction;

import java.util.List;

public interface ExecuteTransaction {
	List<Transaction> executeTransactions(List<Transaction> transactions);
}

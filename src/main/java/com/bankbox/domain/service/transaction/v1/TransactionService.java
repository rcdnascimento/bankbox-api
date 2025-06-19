package com.bankbox.domain.service.transaction.v1;

import com.bankbox.domain.entity.Transaction;
import com.bankbox.domain.exception.CustomerNotFoundException;
import com.bankbox.infra.repository.TransactionRepository;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import com.bankbox.domain.service.transaction.ExecuteTransaction;
import com.bankbox.domain.service.transaction.RetrieveTransaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService implements ExecuteTransaction, RetrieveTransaction {

	private final TransactionRepository transactionRepository;
	private final RetrieveCustomer retrieveCustomer;

	public TransactionService(
		TransactionRepository transactionRepository,
		RetrieveCustomer retrieveCustomer
	) {
		this.transactionRepository = transactionRepository;
		this.retrieveCustomer = retrieveCustomer;
	}

	@Override
	@Transactional
	public List<Transaction> executeTransactions(List<Transaction> transactions) {
		transactions.forEach(Transaction::execute);
		return transactionRepository.saveAll(transactions);
	}

	@Override
	public Transaction saveTransaction(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

	@Override
	public List<Transaction> retrieveByCustomer(Long id) {
		if (!retrieveCustomer.existsById(id)) {
			throw new CustomerNotFoundException();
		}

		return transactionRepository.findBySourceOrBeneficiary(id);
	}
}

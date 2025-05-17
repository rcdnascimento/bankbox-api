package com.bankbox.infra.converter;

import com.bankbox.domain.entity.BankAccount;
import com.bankbox.domain.entity.Transaction;
import com.bankbox.domain.entity.TransactionType;
import com.bankbox.infra.dto.DateTransactionsResponse;
import com.bankbox.infra.dto.TransactionFlow;
import com.bankbox.infra.dto.TransactionRequest;
import com.bankbox.infra.dto.TransactionResponse;
import com.bankbox.domain.service.bankaccount.RetrieveBankAccount;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TransactionConverter {
	private final RetrieveBankAccount retrieveBankAccount;
	private final BankAccountConverter bankAccountConverter;

	public TransactionConverter(
		RetrieveBankAccount retrieveBankAccount,
		BankAccountConverter bankAccountConverter
	) {
		this.retrieveBankAccount = retrieveBankAccount;
		this.bankAccountConverter = bankAccountConverter;
	}

	public List<Transaction> toModel(List<TransactionRequest> requests) {
		return requests.stream()
			.map(this::toModel)
			.collect(Collectors.toList());
	}

	public Transaction toModel(TransactionRequest request) {
		BankAccount source = retrieveBankAccount.retrieveById(request.sourceId);
		BankAccount beneficiary = retrieveBankAccount.retrieveById(request.beneficiaryId);
		return new Transaction(source, beneficiary, TransactionType.valueOf(request.type), request.value);
	}

	public List<TransactionResponse> toResponse(List<Transaction> transactions, Long customerId) {
		List<TransactionResponse> transactionsResponse = transactions.stream()
			.map(transaction -> toResponse(transaction, customerId))
			.collect(Collectors.toList());
		return transformFlowBothToInboundAndOutbound(transactionsResponse);
	}

	public TransactionResponse toResponse(Transaction transaction, Long customerId) {
		TransactionResponse response = new TransactionResponse();
		response.id = transaction.getId();
		response.sourceId = transaction.getSource().getId();
		response.beneficiaryId = transaction.getBeneficiary().getId();
		response.source = bankAccountConverter.toBasicResponse(transaction.getSource());
		response.beneficiary = bankAccountConverter.toBasicResponse(transaction.getBeneficiary());
		response.type = transaction.getType();
		response.value = transaction.getValue();
		response.performedAt = transaction.getPerformedAt();
		response.flow = transaction.extractFlowForCustomer(customerId);
		return response;
	}

	public List<DateTransactionsResponse> toDateTransactionResponse(List<Transaction> transactions, Long customerId) {
		List<TransactionResponse> transactionsConverted = toResponse(transactions, customerId);
		Map<LocalDate, List<TransactionResponse>> map = mapTransactionsByDate(transactionsConverted);
		List<DateTransactionsResponse> dateTransactions = new ArrayList<>();
		map.forEach((performedAt, transactionsResponse) -> dateTransactions.add(new DateTransactionsResponse(performedAt, transactionsResponse)));
		return sort(dateTransactions);
	}

	public List<DateTransactionsResponse> sort(List<DateTransactionsResponse> dateTransactions) {
		List<DateTransactionsResponse> sortedTransactions = dateTransactions.stream()
			.sorted(Comparator.comparing(it -> it.performedAt))
			.peek(it -> {
				it.transactions = it.transactions.stream().sorted(Comparator.comparing(transaction -> transaction.performedAt)).collect(Collectors.toList());
				Collections.reverse(it.transactions);
			})
			.collect(Collectors.toList());
		Collections.reverse(sortedTransactions);
		return sortedTransactions;
	}

	private List<TransactionResponse> transformFlowBothToInboundAndOutbound(final List<TransactionResponse> transactions) {
		List<TransactionResponse> inboundTransactions = transactions.stream()
			.filter(it -> it.flow == TransactionFlow.BOTH)
			.peek(it -> it.flow = TransactionFlow.INBOUND)
			.collect(Collectors.toList());
		List<TransactionResponse> outboundTransactions = inboundTransactions.stream()
			.map(TransactionResponse::new)
			.peek(transaction -> transaction.flow = TransactionFlow.OUTBOUND)
			.collect(Collectors.toList());

		transactions.addAll(outboundTransactions);
		return transactions;
	}

	private Map<LocalDate, List<TransactionResponse>> mapTransactionsByDate(List<TransactionResponse> transactions) {
		Map<LocalDate, List<TransactionResponse>> map = new HashMap<>();
		transactions.forEach(it -> {
			List<TransactionResponse> actualList = map.get(it.performedAt.toLocalDate());
			if (Objects.isNull(actualList)) actualList = new ArrayList<>();
			actualList.add(it);
			map.put(it.performedAt.toLocalDate(), actualList);
		});
		return map;
	}
}

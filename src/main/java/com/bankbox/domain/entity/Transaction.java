package com.bankbox.domain.entity;

import com.bankbox.infra.dto.TransactionFlow;
import com.bankbox.domain.exception.BalanceNotEnoughException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "source_id")
	private BankAccount source;
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "beneficiary_id")
	private BankAccount beneficiary;
	@NotNull
	private BigDecimal value;
	@NotNull
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	@NotNull
	private LocalDateTime performedAt;

	public Transaction(BankAccount source, BankAccount beneficiary, TransactionType type, BigDecimal value) {
		validateSourceBalanceAvailability(source, value);
		this.source = source;
		this.beneficiary = beneficiary;
		this.type = type;
		this.value = value;
		this.performedAt = LocalDateTime.now().minusHours(3);
	}

	public void execute() {
		this.source.transfer(beneficiary, value);
	}

	public void validateSourceBalanceAvailability(BankAccount source, BigDecimal balance) {
		if (balance.compareTo(source.getBalance()) > 0)
			throw new BalanceNotEnoughException();
	}

	public TransactionFlow extractFlowForCustomer(Long customerId) {
		boolean isSourceOwner = Objects.equals(this.source.getOwner().getId(), customerId);
		boolean isBeneficiaryOwner = Objects.equals(this.beneficiary.getOwner().getId(), customerId);

		if (!isSourceOwner && !isBeneficiaryOwner) {
			throw new IllegalArgumentException("Customer not related to transaction.");
		}

		if (isBeneficiaryOwner && isSourceOwner) {
			return TransactionFlow.BOTH;
		} else if (isSourceOwner) {
			return TransactionFlow.OUTBOUND;
		}

		return TransactionFlow.INBOUND;
	}
}

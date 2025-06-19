package com.bankbox.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreditCardInstallment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private BigDecimal value;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private InstallmentStatusEnum status;

	@Column(name = "`number`", nullable = false)
	private Integer number;

	@ManyToOne
	@JoinColumn(name = "transaction_id", nullable = false)
	private CreditCardTransaction transaction;
}

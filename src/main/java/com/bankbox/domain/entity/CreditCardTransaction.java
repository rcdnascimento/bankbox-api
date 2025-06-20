package com.bankbox.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreditCardTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private CategoryEnum category;

	private BigDecimal value;

	@Column(name = "merchant_name")
	private String merchantName;

	@OneToMany(mappedBy = "transaction")
	private List<CreditCardInstallment> installments;

	@ManyToOne
	@JoinColumn(name = "invoice_id")
	private CreditCardInvoice invoice;

	@ManyToOne
	@JoinColumn(name = "credit_card_id", nullable = false)
	private CreditCard creditCard;

	@Column(name = "processed_at")
	private LocalDateTime processedAt;
}

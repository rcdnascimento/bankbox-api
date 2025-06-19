package com.bankbox.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkedCreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "credit_card_id", nullable = false)
	private CreditCard creditCard;

	@Column(name = "`allowedLimit`")
	private BigDecimal allowedLimit;

	@ManyToOne
	@JoinColumn(name = "unified_credit_card_id", nullable = false)
	private UnifiedCreditCard unifiedCreditCard;
}

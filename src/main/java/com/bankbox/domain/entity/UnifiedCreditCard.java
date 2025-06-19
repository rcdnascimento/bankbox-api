package com.bankbox.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedCreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@NotNull
	private String number;

	@NotNull
	private String expiration;

	@NotNull
	@Column(name = "security_number")
	private Integer securityNumber;

	@Column(name = "`limit`")
	private BigDecimal limit;

	@OneToMany(mappedBy = "unifiedCreditCard", cascade = CascadeType.PERSIST)
	private List<LinkedCreditCard> linkedCreditCards;

	public void setLinkedCreditCards(List<LinkedCreditCard> linkedCreditCards) {
		this.linkedCreditCards = linkedCreditCards;
		this.limit = linkedCreditCards.stream()
			.map(LinkedCreditCard::getAllowedLimit)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		for (LinkedCreditCard linkedCard : linkedCreditCards) {
			linkedCard.setUnifiedCreditCard(this);
		}
	}
}

package com.bankbox.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
	private Customer customer;
	@NotNull
	private String number;
	@NotNull
	private String expiration;
	@NotNull
	private int securityNumber;
	@OneToMany(mappedBy = "unifiedCreditCard", cascade = CascadeType.PERSIST)
	private List<LinkedCreditCard> linkedCreditCards;
}

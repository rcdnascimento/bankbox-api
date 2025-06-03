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
	@OneToMany(mappedBy = "transaction")
	private List<CreditCardInstallment> installments;
	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false)
	private CreditCardInvoice invoice;
	@Column(name = "paid_at")
	private LocalDateTime paidAt;
}

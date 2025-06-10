package com.bankbox.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreditCardInvoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "`month`")
	private YearMonth month;

	@Column(name = "due_date")
	private LocalDateTime dueDate;

	@Column(name = "paid_at")
	private LocalDateTime paidAt;

	@ManyToOne
	@JoinColumn(name = "credit_card_id", nullable = false)
	private CreditCard creditCard;
}

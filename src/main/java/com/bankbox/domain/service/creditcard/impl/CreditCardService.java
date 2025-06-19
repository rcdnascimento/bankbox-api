package com.bankbox.domain.service.creditcard.impl;

import com.bankbox.domain.entity.*;
import com.bankbox.domain.exception.CreditCardNotFoundException;
import com.bankbox.domain.exception.CustomerNotFoundException;
import com.bankbox.domain.service.creditcard.RetrieveCreditCard;
import com.bankbox.infra.repository.CreditCardRepository;
import com.bankbox.domain.service.creditcard.PersistCreditCard;
import com.bankbox.infra.repository.CustomerRepository;
import com.bankbox.infra.repository.UnifiedCreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditCardService implements RetrieveCreditCard, PersistCreditCard {

	private final CreditCardRepository creditCardRepository;
	private final CustomerRepository customerRepository;

	private static final String DEFAULT_EXPIRATION = "2031-06";
	private final UnifiedCreditCardRepository unifiedCreditCardRepository;


	@Override
	public List<CreditCard> retrieveByCustomerId(Long customerId) {
		return creditCardRepository.findByCustomerId(customerId);
	}

	@Override
	public CreditCard addCreditCard(CreditCard creditCard) {
		if (!customerRepository.existsById(creditCard.getCustomer().getId())) {
			throw new CustomerNotFoundException();
		}

		return insertCreditCard(creditCard);
//		return creditCardRepository.save(creditCard);
	}

	@Override
	public CreditCard retrieveById(Long id) {
		return creditCardRepository.findById(id).orElseThrow(CreditCardNotFoundException::new);
	}

	@Override
	public UnifiedCreditCard generateUnifiedCardForCustumer(Long customerId) {
		Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(CustomerNotFoundException::new);
		Optional<UnifiedCreditCard> currentUnifiedCard = unifiedCreditCardRepository.findByCustomerId(customerId);

		if (currentUnifiedCard.isPresent()) {
			UnifiedCreditCard currentUnifiedCreditCard = currentUnifiedCard.get();
			currentUnifiedCreditCard.setLinkedCreditCards(extractLinkedCreditCards(customer.getCreditCards()));
			return currentUnifiedCreditCard;
		}

		UnifiedCreditCard unifiedCard = new UnifiedCreditCard();
		unifiedCard.setLinkedCreditCards(extractLinkedCreditCards(customer.getCreditCards()));
		unifiedCard.setNumber(generateCardNumber());
		unifiedCard.setSecurityNumber(generateSecurityNumber());
		unifiedCard.setExpiration(DEFAULT_EXPIRATION);
		unifiedCard.setLimit(customer.getTotalLimitFromAllCreditCards());
		unifiedCard.setCustomer(customer);

		return unifiedCreditCardRepository.save(unifiedCard);
	}

	private List<LinkedCreditCard> extractLinkedCreditCards(List<CreditCard> creditCards) {
		return creditCards.stream()
			.map(card -> LinkedCreditCard.builder()
				.creditCard(card)
				.allowedLimit(Objects.nonNull(card.getLimit()) ? card.getLimit() : BigDecimal.ZERO)
				.build())
			.collect(Collectors.toList());
	}

	private CreditCard insertCreditCard(CreditCard creditCard) {
		creditCardRepository.insertCreditCard(
			creditCard.getOwnerName(),
			creditCard.getNumber(),
			creditCard.getExpiration(),
			creditCard.getSecurityNumber(),
			creditCard.getType().name(),
			creditCard.getBrand(),
			creditCard.getLimit(),
			creditCard.getCustomer().getId()
		);

		return creditCardRepository.retrieveLastCreated();
	}

	private Integer generateSecurityNumber() {
		return 100 + new Random().nextInt(899);
	}

	private String generateCardNumber() {
		String number = "";
		for (int i = 0; i < 4; i++) {
			number = number.concat(String.valueOf(1000 + new Random().nextInt(8999)));
		}
		return number;
	}
}

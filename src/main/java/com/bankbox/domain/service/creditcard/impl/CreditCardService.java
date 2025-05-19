package com.bankbox.domain.service.creditcard.impl;

import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.entity.CreditCard;
import com.bankbox.domain.entity.CreditCardType;
import com.bankbox.domain.exception.CustomerNotFoundException;
import com.bankbox.domain.service.creditcard.RetrieveCreditCard;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import com.bankbox.infra.repository.CreditCardRepository;
import com.bankbox.domain.service.creditcard.PersistCreditCard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class CreditCardService implements RetrieveCreditCard, PersistCreditCard {

	private final CreditCardRepository creditCardRepository;
	private final RetrieveCustomer retrieveCustomer;

	private static final String BANKBOX_BRAND = "BANKBOX";
	private static final String DEFAULT_EXPIRATION = "2031-06";

	public CreditCardService(CreditCardRepository creditCardRepository, RetrieveCustomer retrieveCustomer) {
		this.creditCardRepository = creditCardRepository;
		this.retrieveCustomer = retrieveCustomer;
	}

	@Override
	public List<CreditCard> findAllByCustomerId(Long customerId) {
		return creditCardRepository.findByCustomerId(customerId);
	}

	@Override
	public CreditCard addCreditCard(CreditCard creditCard) {
		if (!retrieveCustomer.existsById(creditCard.getCustomer().getId())) {
			throw new CustomerNotFoundException();
		}

		return insertCreditCard(creditCard);
	}

	@Override
	public CreditCard generateUnifiedCardForCustumer(Long customerId) {
		Customer customer = retrieveCustomer.retrieveById(customerId);
		Optional<CreditCard> currentUnifiedCard = customer.getCreditCards().stream()
			.filter(card -> Objects.equals(card.brand, BANKBOX_BRAND)).findFirst();

		if (currentUnifiedCard.isPresent()) {
			return currentUnifiedCard.get();
		}

		CreditCard unifiedCard = new CreditCard();
		unifiedCard.setOwnerName(customer.getName());
		unifiedCard.setNumber(generateCardNumber());
		unifiedCard.setSecurityNumber(generateSecurityNumber());
		unifiedCard.setBrand(BANKBOX_BRAND);
		unifiedCard.setExpiration(DEFAULT_EXPIRATION);
		unifiedCard.setType(CreditCardType.VIRTUAL);
		unifiedCard.setLimit(customer.getTotalLimitFromAllCreditCards());
		unifiedCard.setCustomer(customer);

		return insertCreditCard(unifiedCard);
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

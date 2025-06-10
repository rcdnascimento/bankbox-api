package com.bankbox.domain.service.creditcard.impl;

import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.entity.CreditCard;
import com.bankbox.domain.entity.CreditCardType;
import com.bankbox.domain.exception.CreditCardNotFoundException;
import com.bankbox.domain.exception.CustomerNotFoundException;
import com.bankbox.domain.service.creditcard.RetrieveCreditCard;
import com.bankbox.infra.repository.CreditCardRepository;
import com.bankbox.domain.service.creditcard.PersistCreditCard;
import com.bankbox.infra.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CreditCardService implements RetrieveCreditCard, PersistCreditCard {

	private final CreditCardRepository creditCardRepository;
	private final CustomerRepository customerRepository;

	private static final String BANKBOX_BRAND = "BANKBOX";
	private static final String DEFAULT_EXPIRATION = "2031-06";


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
	}

	@Override
	public CreditCard retrieveById(Long id) {
		return creditCardRepository.findById(id).orElseThrow(CreditCardNotFoundException::new);
	}

	@Override
	public CreditCard generateUnifiedCardForCustumer(Long customerId) {
		Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(CustomerNotFoundException::new);
		Optional<CreditCard> currentUnifiedCard = customer.getCreditCards().stream()
			.filter(card -> Objects.equals(card.brand, BANKBOX_BRAND)).findFirst();

		if (currentUnifiedCard.isPresent()) {
			CreditCard currentUnifiedCreditCard = currentUnifiedCard.get();
			currentUnifiedCreditCard.setLimit(customer.getTotalLimitFromAllCreditCards());
			return currentUnifiedCreditCard;
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

package com.bankbox.domain.service.creditcard.impl;

import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.entity.CreditCard;
import com.bankbox.domain.entity.CreditCardType;
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

	public CreditCardService(CreditCardRepository creditCardRepository, RetrieveCustomer retrieveCustomer) {
		this.creditCardRepository = creditCardRepository;
		this.retrieveCustomer = retrieveCustomer;
	}

	@Override
	public List<CreditCard> findAllByCustomerId(Long customerId) {
		return creditCardRepository.findByCustomerId(customerId);
	}

	@Override
	public CreditCard generateUnifiedCardForCustumer(Long customerId) {
		Customer customer = retrieveCustomer.retrieveById(customerId);
		Optional<CreditCard> currentUnifiedCard = customer.getCreditCards().stream()
			.filter(card -> Objects.equals(card.brand, "BANKBOX")).findFirst();
		if (currentUnifiedCard.isPresent()) return currentUnifiedCard.get();
		CreditCard unifiedCard = new CreditCard();
		unifiedCard.setOwner(customer.getName());
		unifiedCard.setNumber(generateCardNumber());
		unifiedCard.setSecurityNumber(100 + new Random().nextInt(899));
		unifiedCard.setBrand("BANKBOX");
		unifiedCard.setExpiration("2031-06");
		unifiedCard.setType(CreditCardType.VIRTUAL);
		unifiedCard.setCreditLimit(customer.getCreditCardsLimit());
		unifiedCard.setCustomer(customer);
		return creditCardRepository.save(unifiedCard);
	}

	private String generateCardNumber() {
		String number = "";
		for (int i = 0; i < 4; i++) {
			number = number.concat(String.valueOf(1000 + new Random().nextInt(8999)));
		}
		return number;
	}
}

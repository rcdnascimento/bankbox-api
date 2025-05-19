package com.bankbox.domain.service.creditcard;

import com.bankbox.domain.entity.CreditCard;

import java.util.List;

public interface RetrieveCreditCard {
	List<CreditCard> retrieveByCustomerId(Long customerId);
}

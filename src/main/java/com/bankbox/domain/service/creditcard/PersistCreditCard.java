package com.bankbox.domain.service.creditcard;

import com.bankbox.domain.entity.CreditCard;

public interface PersistCreditCard {
	CreditCard generateUnifiedCardForCustumer(Long customerId);
	CreditCard addCreditCard(CreditCard creditCard);
}

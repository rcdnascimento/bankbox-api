package com.bankbox.domain.service.creditcard;

import com.bankbox.domain.entity.CreditCard;
import com.bankbox.domain.entity.UnifiedCreditCard;

public interface PersistCreditCard {
	UnifiedCreditCard generateUnifiedCardForCustumer(Long customerId);
	CreditCard addCreditCard(CreditCard creditCard);
}

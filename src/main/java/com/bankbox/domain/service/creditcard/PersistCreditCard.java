package com.bankbox.domain.service.creditcard;

import com.bankbox.domain.entity.CreditCard;

public interface PersistCreditCard {
	CreditCard generateUnifiedCardForCustumer(Long customerId);
}

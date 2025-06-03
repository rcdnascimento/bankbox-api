package com.bankbox.domain.service.customer;

import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.entity.CustomerRegistration;

public interface CreateCustomer {
	Customer createCustomer(Customer customer);
	CustomerRegistration registerCustomer(CustomerRegistration customerRegistration);
	Customer confirmRegistration(Long registrationId, String code);
}

package com.bankbox.infra.resource.v2;

import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.service.creditcard.PersistCreditCard;
import com.bankbox.domain.service.customer.CreateCustomer;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import com.bankbox.domain.service.customer.impl.CustomerService;
import com.bankbox.infra.converter.CreditCardConverter;
import com.bankbox.infra.converter.CustomerConverter;
import com.bankbox.infra.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v2/customers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CustomerResourceV2 {

	private final RetrieveCustomer retrieveCustomer;
	private final CreateCustomer createCustomer;
	private final CustomerConverter customerConverter;
	private final PersistCreditCard persistCreditCard;
	private final CreditCardConverter creditCardConverter;
	private final CustomerService customerService;

	private List<Customer> customersCache = new ArrayList<>();

	@GetMapping
	public ResponseEntity<List<CustomerBasicResponse>> retrieveAll() {
		if (customersCache.isEmpty()) {
			List<Customer> customers = retrieveCustomer.retrieveAll();
			customersCache = customers;
			return ResponseEntity.ok(customerConverter.toBasicResponse(customers));
		}

		return ResponseEntity.ok(customerConverter.toBasicResponse(customersCache));
	}
}

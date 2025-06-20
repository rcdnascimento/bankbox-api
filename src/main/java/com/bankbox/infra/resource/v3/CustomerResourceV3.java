package com.bankbox.infra.resource.v3;

import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.service.creditcard.PersistCreditCard;
import com.bankbox.domain.service.customer.CreateCustomer;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import com.bankbox.domain.service.customer.impl.CustomerService;
import com.bankbox.infra.converter.CreditCardConverter;
import com.bankbox.infra.converter.CustomerConverter;
import com.bankbox.infra.dto.CustomerBasicResponse;
import com.bankbox.infra.dto.PaginatedCustomerFullResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/customers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CustomerResourceV3 {

	private final RetrieveCustomer retrieveCustomer;
	private final CreateCustomer createCustomer;
	private final CustomerConverter customerConverter;
	private final PersistCreditCard persistCreditCard;
	private final CreditCardConverter creditCardConverter;
	private final CustomerService customerService;

	@GetMapping
	public ResponseEntity<PaginatedCustomerFullResponse> retrieveAll(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer size) {
		Page<Customer> customers = retrieveCustomer.retrievePaginated(page, size);
		return ResponseEntity.ok(buildPaginatedResponse(customers, page, size));
	}

	private PaginatedCustomerFullResponse buildPaginatedResponse(Page<Customer> customers, int page, int size) {
		PaginatedCustomerFullResponse response = new PaginatedCustomerFullResponse();
		response.customers = customerConverter.toFullResponse(customers.getContent());
		response.totalPages = customers.getTotalPages();
		response.totalElements = customers.getTotalElements();
		response.size = size;
		return response;
	}
}

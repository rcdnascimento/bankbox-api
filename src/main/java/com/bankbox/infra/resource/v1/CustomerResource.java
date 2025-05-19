package com.bankbox.infra.resource.v1;

import com.bankbox.domain.entity.CreditCard;
import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.service.creditcard.PersistCreditCard;
import com.bankbox.infra.converter.CreditCardConverter;
import com.bankbox.infra.converter.CustomerConverter;
import com.bankbox.infra.dto.*;
import com.bankbox.domain.service.customer.CreateCustomer;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/customers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CustomerResource {

	private final RetrieveCustomer retrieveCustomer;
	private final CreateCustomer createCustomer;
	private final CustomerConverter customerConverter;
	private final PersistCreditCard persistCreditCard;
	private final CreditCardConverter creditCardConverter;

	@GetMapping
	public ResponseEntity<List<CustomerDTO>> retrieveAll() {
		List<Customer> customers = retrieveCustomer.retrieveAll();
		return ResponseEntity.ok(customerConverter.toDTO(customers));
	}

	@PostMapping
	public ResponseEntity<CustomerDTO> createCustomer(@Valid  @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
		Customer customer = customerConverter.toCustomer(customerRegistrationRequest);
		Customer customerCreated = createCustomer.createCustomer(customer);
		return ResponseEntity.ok(customerConverter.toDTO(customerCreated));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerDTO> retrieveCustomer(@PathVariable Long id) {
		Customer customerFound = retrieveCustomer.retrieveById(id);
		return ResponseEntity.ok(customerConverter.toDTO(customerFound));
	}

	@GetMapping("/{cpf}/basic")
	public ResponseEntity<CustomerBasicDTO> retrieveCustomerBasic(@PathVariable String cpf) {
		Customer customerFound = retrieveCustomer.retrieveByCpf(cpf);
		return ResponseEntity.ok(customerConverter.toBasic(customerFound));
	}

	@GetMapping("/{id}/balance")
	public ResponseEntity<BalanceDetailsResponse> retrieveCustomerBalanceDetails(@PathVariable Long id) {
		Customer customerFound = retrieveCustomer.retrieveById(id);
		return ResponseEntity.ok(customerConverter.toBalanceDetails(customerFound));
	}

	@PostMapping("/{id}/credit-cards")
	public ResponseEntity<CreditCardResponse> addCreditCard(@PathVariable Long id, @RequestBody CreditCardRequest creditCardRequest) {
		CreditCard creditCardEntity = creditCardConverter.toEntity(creditCardRequest, id);
		CreditCard creditCardCreated = persistCreditCard.addCreditCard(creditCardEntity);

		return ResponseEntity.ok(creditCardConverter.toResponse(creditCardCreated));
	}
}

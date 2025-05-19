package com.bankbox.infra.resource.v1;

import com.bankbox.infra.converter.CreditCardConverter;
import com.bankbox.domain.entity.CreditCard;
import com.bankbox.infra.dto.CreditCardRequest;
import com.bankbox.infra.dto.CreditCardResponse;
import com.bankbox.domain.service.creditcard.PersistCreditCard;
import com.bankbox.domain.service.creditcard.RetrieveCreditCard;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/credit-cards")
@CrossOrigin(origins = "*")
public class CreditCardResource {

	private final RetrieveCreditCard retrieveCreditCard;
	private final CreditCardConverter creditCardConverter;
	private final PersistCreditCard persistCreditCard;

	public CreditCardResource(
		RetrieveCreditCard retrieveCreditCard,
		PersistCreditCard persistCreditCard,
		CreditCardConverter creditCardConverter
	) {
		this.retrieveCreditCard = retrieveCreditCard;
		this.persistCreditCard = persistCreditCard;
		this.creditCardConverter = creditCardConverter;
	}

	@GetMapping
	public ResponseEntity<List<CreditCardResponse>> retrieveCreditCards(@RequestParam(value = "customer_id", required = true) Long customerId) {
		List<CreditCard> creditCards = retrieveCreditCard.findAllByCustomerId(customerId);
		return ResponseEntity.ok(creditCardConverter.toResponse(creditCards));
	}

	@PostMapping("/unified")
	public ResponseEntity<CreditCardResponse> generateUnifiedCard(@RequestParam(value = "customer_id", required = true) Long customerId) {
		CreditCard unifiedCreditCard = persistCreditCard.generateUnifiedCardForCustumer(customerId);
		return ResponseEntity.ok(creditCardConverter.toResponse(unifiedCreditCard));
	}
}

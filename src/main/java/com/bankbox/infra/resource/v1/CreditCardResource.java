package com.bankbox.infra.resource.v1;

import com.bankbox.domain.entity.CreditCardInvoice;
import com.bankbox.domain.entity.CreditCardTransaction;
import com.bankbox.domain.entity.UnifiedCreditCard;
import com.bankbox.domain.service.creditcardinvoice.CreditCardInvoiceService;
import com.bankbox.domain.service.creditcardtransaction.PersistCreditCardTransaction;
import com.bankbox.domain.service.creditcardtransaction.RetrieveCreditCardTransaction;
import com.bankbox.infra.converter.CreditCardConverter;
import com.bankbox.domain.entity.CreditCard;
import com.bankbox.infra.converter.CreditCardInvoiceConverter;
import com.bankbox.infra.converter.CreditCardTransactionConverter;
import com.bankbox.infra.dto.*;
import com.bankbox.domain.service.creditcard.PersistCreditCard;
import com.bankbox.domain.service.creditcard.RetrieveCreditCard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/credit-cards")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CreditCardResource {

	private final RetrieveCreditCard retrieveCreditCard;
	private final CreditCardConverter creditCardConverter;
	private final PersistCreditCard persistCreditCard;
	private final RetrieveCreditCardTransaction retrieveCreditCardTransaction;
	private final PersistCreditCardTransaction persistCreditCardTransaction;
	private final CreditCardTransactionConverter creditCardTransactionConverter;
	private final CreditCardInvoiceService creditCardInvoiceService;
	private final CreditCardInvoiceConverter creditCardInvoiceConverter;

	@GetMapping
	public ResponseEntity<List<CreditCardResponse>> retrieveCreditCards(@RequestParam(value = "customer_id", required = true) Long customerId) {
		List<CreditCard> creditCards = retrieveCreditCard.retrieveByCustomerId(customerId);
		return ResponseEntity.ok(creditCardConverter.toResponse(creditCards));
	}

	@PostMapping("/unified")
	public ResponseEntity<UnifiedCreditCardResponse> generateUnifiedCard(@RequestParam(value = "customer_id", required = true) Long customerId) {
		UnifiedCreditCard unifiedCreditCard = persistCreditCard.generateUnifiedCardForCustumer(customerId);
		return ResponseEntity.ok(creditCardConverter.toResponse(unifiedCreditCard));
	}

	@GetMapping("/{id}/transactions")
	public ResponseEntity<List<CreditCardTransactionResponse>> retrieveCreditCardTransactions(
		@PathVariable("id") Long creditCardId
	) {
		List<CreditCardTransaction> transactions = retrieveCreditCardTransaction.retrieveByCreditCard(creditCardId);
		return ResponseEntity.ok(creditCardTransactionConverter.toResponse(transactions));
	}

	@PostMapping("/{id}/transactions")
	public ResponseEntity<CreditCardTransactionResponse> createTransaction(
		@PathVariable Long id,
		@Valid @RequestBody CreditCardTransactionRequest transaction
	) {
		transaction.creditCardId = id;

		CreditCardTransaction createdTransaction = persistCreditCardTransaction.createTransaction(transaction);
		CreditCardTransactionResponse response = creditCardTransactionConverter.toResponse(createdTransaction);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}/invoices")
	public ResponseEntity<List<CreditCardInvoiceResponse>> retrieveInvoices(@PathVariable Long id, @RequestParam(required = false) String yearMonth) {
		YearMonth month = Optional.ofNullable(yearMonth)
			.map(YearMonth::parse)
			.orElse(null);

		List<CreditCardInvoice> invoices = creditCardInvoiceService.retrieveInvoicesByCreditCardAndMonth(id, month);
		return ResponseEntity.ok(creditCardInvoiceConverter.toResponse(invoices));
	}
}

package com.bankbox.infra.resource.v1;

import com.bankbox.infra.converter.BankAccountConverter;
import com.bankbox.domain.entity.BankAccount;
import com.bankbox.infra.dto.BankAccountRequest;
import com.bankbox.infra.dto.BankAccountResponse;
import com.bankbox.domain.service.bankaccount.PersistBankAccount;
import com.bankbox.domain.service.bankaccount.RetrieveBankAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/bank-accounts")
public class BankAccountResource {

	private final PersistBankAccount persistBankAccount;
	private final RetrieveBankAccount retrieveBankAccount;
	private final BankAccountConverter bankAccountConverter;

	public BankAccountResource(
		PersistBankAccount persistBankAccount,
		RetrieveBankAccount retrieveBankAccount,
		BankAccountConverter bankAccountConverter
	) {
		this.persistBankAccount = persistBankAccount;
		this.retrieveBankAccount = retrieveBankAccount;
		this.bankAccountConverter = bankAccountConverter;
	}

	@GetMapping
	public ResponseEntity<List<BankAccountResponse>> getBankAccounts(
		@RequestParam(value = "user_id", required = false) Long userId,
		@RequestParam(value = "pix_key", required = false) String pixKey,
		@RequestParam(value = "bank", required = false) String bank,
		@RequestParam(value = "agency", required = false) String agency,
		@RequestParam(value = "account", required = false) String account
	) {
		List<BankAccount> bankAccountsFound = retrieveAccountsApplyingFilters(userId, pixKey, bank, agency, account);
		return ResponseEntity.ok(bankAccountConverter.toResponse(bankAccountsFound));
	}

	@PostMapping
	public ResponseEntity<BankAccountResponse> addBank(@RequestBody BankAccountRequest request) {
		BankAccount bankAccount = bankAccountConverter.toDomain(request);
		BankAccount createdBankAccount = persistBankAccount.addBankAccount(bankAccount);
		return ResponseEntity.ok(bankAccountConverter.toResponse(createdBankAccount));
	}

	private List<BankAccount> retrieveAccountsApplyingFilters(
		Long userId,
		String pixKey,
		String bank,
		String agency,
		String account
	) {
		if (Objects.nonNull(userId)) {
			return retrieveBankAccount.retrieveByUser(userId);
		};

		if (Objects.nonNull(pixKey)) {
			return List.of(retrieveBankAccount.retrieveByPixKey(pixKey));
		}

		if (Objects.nonNull(agency) && Objects.nonNull(account) && Objects.nonNull(bank)) {
			return List.of(retrieveBankAccount.retrieveByAgencyAndAccount(bank, agency, account));
		}

		return List.of();
	}
}

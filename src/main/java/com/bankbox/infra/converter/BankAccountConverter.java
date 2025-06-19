package com.bankbox.infra.converter;

import com.bankbox.domain.entity.BankAccount;
import com.bankbox.domain.entity.Consent;
import com.bankbox.domain.entity.ConsentRole;
import com.bankbox.infra.dto.BankAccountBasicResponse;
import com.bankbox.infra.dto.BankAccountRequest;
import com.bankbox.infra.dto.BankAccountResponse;
import com.bankbox.infra.dto.ConsentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BankConverter.class, CustomerConverter.class})
public interface BankAccountConverter {
	@Mapping(source = "owner.id", target = "customerId")
	BankAccountResponse toResponse(BankAccount bankAccount);
	List<BankAccountResponse> toResponse(List<BankAccount> bankAccount);
	@Mapping(source = "customerId", target = "owner.id")
	@Mapping(target = "consent", expression = "java(consentConsentRequestToEntity(bankAccountRequest.consent))")
	@Mapping(source = "bankId", target = "bank.id")
	BankAccount toDomain(BankAccountRequest bankAccountRequest);
	@Mapping(expression = "java(bankAccount.getOwner().getFirstName())", target = "customerFirstName")
	BankAccountBasicResponse toBasicResponse(BankAccount bankAccount);

	default Consent consentConsentRequestToEntity(ConsentRequest consentRequest) {
		Consent consent = new Consent();
		consent.setCode(consentRequest.code);

		consentRequest.roles.forEach((consentId) -> {
			ConsentRole role = new ConsentRole();
			role.setId(consentId);
			consent.addRole(role);
		});

		return consent;
	}
}

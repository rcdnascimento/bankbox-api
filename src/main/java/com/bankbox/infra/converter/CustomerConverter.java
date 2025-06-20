package com.bankbox.infra.converter;

import com.bankbox.domain.entity.BankAccountType;
import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.entity.CustomerRegistration;
import com.bankbox.infra.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = BankAccountType.class)
public interface CustomerConverter {
	@Mapping(expression = "java(customer.getFirstName())", target = "firstName")
	CustomerBasicDTO toBasic(Customer customer);

	@Mapping(expression = "java(customer.getFirstName())", target = "firstName")
	CustomerFullResponse toFullResponse(Customer customer);
	List<CustomerFullResponse> toFullResponse(List<Customer> customers);

	@Mapping(expression = "java(customer.getFirstName())", target = "firstName")
	CustomerBasicResponse toBasicResponse(Customer customer);
	List<CustomerBasicResponse> toBasicResponse(List<Customer> customers);

	Customer toCustomer(CustomerRegistrationRequest customerRegistrationRequest);

	CustomerRegistration toEntity(CustomerRegistrationRequest registrationRequest);

	@Mapping(source = "id", target = "registrationId")
	CustomerRegistrationResponse toResponse(CustomerRegistration customerRegistration);

	@Mapping(source = "id", target = "customerId")
	@Mapping(expression = "java(customer.getBalance())", target = "totalBalance")
	@Mapping(expression = "java(customer.getBalanceFrom(BankAccountType.CHECKING))", target = "checkingBalance")
	@Mapping(expression = "java(customer.getBalanceFrom(BankAccountType.SAVINGS))", target = "savingsBalance")
	BalanceDetailsResponse toBalanceDetails(Customer customer);
}

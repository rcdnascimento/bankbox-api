package com.bankbox.infra.converter;

import com.bankbox.domain.entity.BankAccountType;
import com.bankbox.domain.entity.Customer;
import com.bankbox.infra.dto.BalanceDetailsResponse;
import com.bankbox.infra.dto.CustomerBasicDTO;
import com.bankbox.infra.dto.CustomerDTO;
import com.bankbox.infra.dto.CustomerRegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = BankAccountType.class)
public interface CustomerConverter {
	@Mapping(expression = "java(customer.getFirstName())", target = "firstName")
	CustomerBasicDTO toBasic(Customer customer);

	@Mapping(expression = "java(customer.getFirstName())", target = "firstName")
	CustomerDTO toDTO(Customer customer);
	List<CustomerDTO> toDTO(List<Customer> customers);

	Customer toCustomer(CustomerRegistrationRequest customerRegistrationRequest);

	@Mapping(expression = "java(customer.getBalance())", target = "totalBalance")
	@Mapping(expression = "java(customer.getBalanceFrom(BankAccountType.CHECKING))", target = "checkingBalance")
	@Mapping(expression = "java(customer.getBalanceFrom(BankAccountType.SAVINGS))", target = "savingsBalance")
	BalanceDetailsResponse toBalanceDetails(Customer customer);
}

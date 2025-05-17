package com.bankbox.domain.service.customer;

import com.bankbox.domain.entity.Customer;

import java.util.List;

public interface RetrieveCustomer {
	List<Customer> retrieveAll();
	Customer retrieveById(Long id);
	Customer retrieveByCpf(String cpf);
	boolean existsById(Long id);
}

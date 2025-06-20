package com.bankbox.domain.service.customer;

import com.bankbox.domain.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RetrieveCustomer {
	List<Customer> retrieveAll();
	Page<Customer> retrievePaginated(int page, int size);
	Customer retrieveById(Long id);
	Customer retrieveByCpf(String cpf);
	Long retrieveLastCustomerId();
	boolean existsById(Long id);
	long countCustomers();
}

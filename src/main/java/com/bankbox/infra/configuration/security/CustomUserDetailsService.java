package com.bankbox.infra.configuration.security;

import com.bankbox.domain.entity.Customer;
import com.bankbox.infra.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		Optional<Customer> customer = customerRepository.findByCpf(cpf);
		if (customer.isEmpty()) {
			throw new IllegalArgumentException("Customer does not exist");
		}

		return UserPrincipal.create(customer.get());
	}
}

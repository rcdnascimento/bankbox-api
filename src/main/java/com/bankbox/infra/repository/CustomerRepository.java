package com.bankbox.infra.repository;

import com.bankbox.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	@Query(value = "SELECT * FROM customer WHERE cpf = ?1", nativeQuery = true)
	Optional<Customer> findCustomerByCpf(String cpf);

	@Query(value = "SELECT * FROM customer WHERE id = ?1", nativeQuery = true)
	Optional<Customer> findCustomerById(Long id);

	@Query(value = "SELECT c FROM Customer c LEFT JOIN FETCH c.creditCards")
	Optional<Customer> findCustomerByIdJoinFetch(Long id);

	@Query(value = "SELECT * FROM customer", nativeQuery = true)
	List<Customer> findAllCustomers();

	@Query(value = "SELECT * FROM customer limit ?1 offset ?2", nativeQuery = true)
	List<Customer> findCustomerPaginated(int limit, int offset);

	@Query(value = "SELECT * FROM customer WHERE id = LAST_INSERT_ID()", nativeQuery = true)
	Customer retrieveLastCreated();

	@Query(value = "SELECT 1 FROM customer WHERE id = ?1", nativeQuery = true)
	Integer customerExistsById(Long id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO customer (name, cpf, password) VALUES (?1, ?2, ?3)", nativeQuery = true)
	void insertCustomer(String name, String cpf, String password);
}

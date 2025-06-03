package com.bankbox.infra.repository;

import com.bankbox.domain.entity.CustomerRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRegistrationRepository extends JpaRepository<CustomerRegistration, Long> {
  Optional<CustomerRegistration> findByIdAndCode(Long id, String code);
}

package com.bankbox.infra.repository;

import com.bankbox.domain.entity.ConsentRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentRoleRepository extends JpaRepository<ConsentRole, Long> {
}

package com.bankbox.infra.repository;

import com.bankbox.domain.entity.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PixKeyRepository extends JpaRepository<PixKey, Long> {
}

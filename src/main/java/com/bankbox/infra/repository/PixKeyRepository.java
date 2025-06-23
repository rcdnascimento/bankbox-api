package com.bankbox.infra.repository;

import com.bankbox.domain.entity.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PixKeyRepository extends JpaRepository<PixKey, Long> {
    Optional<PixKey> findByKey(String key);
}

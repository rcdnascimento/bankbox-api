package com.bankbox.infra.repository;

import com.bankbox.domain.entity.BankAccount;
import com.bankbox.domain.entity.BankName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
	@Query(value = "SELECT * FROM bank_account WHERE owner_id = ?1", nativeQuery = true)
	List<BankAccount> findByOwnerId(Long id);
	@Query(value = "SELECT * FROM bank_account WHERE pix_key = ?1", nativeQuery = true)
	Optional<BankAccount> findByPixKey(String pixKey);
	@Query(value = "SELECT * FROM bank_account WHERE bank_name = ?1 AND agency = ?2 AND account = ?3", nativeQuery = true)
	Optional<BankAccount> findByBankNameAndAgencyAndAccount(BankName bankName, String agency, String account);
	@Query(value = "SELECT * FROM bank_account WHERE id = LAST_INSERT_ID()", nativeQuery = true)
	BankAccount retrieveLastCreated();

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO bank_account (bank_name, agency, account, type, balance, owner_id) VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
	void insert(String bankName, String agency, String account, String type, BigDecimal balance, Long ownerId);
}

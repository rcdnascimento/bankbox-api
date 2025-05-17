package com.bankbox.converter;

import com.bankbox.domain.entity.BankAccount;
import com.bankbox.domain.entity.BankAccountType;
import com.bankbox.domain.entity.BankName;
import com.bankbox.domain.entity.Customer;
import com.bankbox.infra.converter.BankAccountConverter;
import com.bankbox.infra.converter.BankAccountConverterImpl;
import com.bankbox.infra.converter.BankConverterImpl;
import com.bankbox.infra.dto.BankAccountResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BankAccountConverterImpl.class, BankConverterImpl.class})
class BankAccountConverterTest {

	@Autowired
	private BankAccountConverter bankAccountConverter;

	@Test
	void mustConvertBankNameToBankResponse() {
		BankAccount bankAccount = new BankAccount(new Customer(), BankName.ITAU, BankAccountType.CHECKING, BigDecimal.ZERO, "123", "4567");
		BankAccountResponse response = bankAccountConverter.toResponse(bankAccount);

		Assertions.assertThat(response.bank).isNotNull();
		Assertions.assertThat(response.bank.name).isEqualTo("ITAU");
		Assertions.assertThat(response.bank.formattedName).isEqualTo("Itaú Unibanco");
		Assertions.assertThat(response.bank.backgroundColor).isEqualTo("#EF761C");
		Assertions.assertThat(response.bank.imgUrl).isEqualTo("../../assets/imgs/banks/itau.png");
	}
}
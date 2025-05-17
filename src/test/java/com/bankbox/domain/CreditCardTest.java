package com.bankbox.domain;

import com.bankbox.domain.entity.CreditCard;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardTest {

	@Test
	void mustSetNumberWhenItHasCorrectSize() {
		String number = "1111222233334444";
		CreditCard creditCard = new CreditCard();
		creditCard.setNumber(number);

		Assertions.assertThat(creditCard.getNumber()).isEqualTo(number);
	}

	@Test
	void mustSetNumberWhenItHasCorrectSizeButHasSpaces() {
		String number = "1111 2222 3333 4444";
		CreditCard creditCard = new CreditCard();
		creditCard.setNumber(number);

		Assertions.assertThat(creditCard.getNumber()).isEqualTo("1111222233334444");
	}

	@Test
	void mustFormatNumberWhenProvidedNameHasSpaces() {
		String number = "1111 2222 3333 4444";
		String formatedNumber = "1111222233334444";
		CreditCard creditCard = new CreditCard();
		creditCard.setNumber(number);

		Assertions.assertThat(creditCard.getNumber()).isEqualTo(formatedNumber);
	}

	@Test
	void mustReturnLastNumbers() {
		String number = "1111222233334444";
		CreditCard creditCard = new CreditCard();
		creditCard.setNumber(number);

		Assertions.assertThat(creditCard.getLastNumbers()).isEqualTo("4444");
	}

	@ParameterizedTest
	@ValueSource(strings = {"111122223333", "11112222333344445"})
	void mustNotSetNumberAndThrowExceptionWhenProvidedNumberHasIncorrectSize(String number) {
		CreditCard creditCard = new CreditCard();

		assertThrows(IllegalArgumentException.class, () -> creditCard.setNumber(number));
	}

	@Test
	void mustSetSecurityNumberWhenSizeIsCorrect() {
		CreditCard creditCard = new CreditCard();
		creditCard.setSecurityNumber(123);

		Assertions.assertThat(creditCard.getSecurityNumber()).isEqualTo(123);
	}

	@ParameterizedTest
	@ValueSource(ints = {12, 1234})
	void mustNotSetSecurityNumberAndThrowExceptionWhenSizeIsIncorrect(int number) {
		CreditCard creditCard = new CreditCard();

		assertThrows(IllegalArgumentException.class, () -> creditCard.setSecurityNumber(number));
	}

}

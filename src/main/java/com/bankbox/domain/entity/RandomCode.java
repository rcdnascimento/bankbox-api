package com.bankbox.domain.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Random;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RandomCode {

	private String code;

	public RandomCode(RandomCodeType type, int length) {
		this.code = generate(type.getCharacters(), length);
	}

	private String generate(String characters, int length) {
		StringBuilder codeBuilder = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			codeBuilder.append(randomChar);
		}

		return codeBuilder.toString();
	}

	@Override
	public String toString() {
		return code;
	}
}

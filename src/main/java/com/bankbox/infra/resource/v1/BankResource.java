package com.bankbox.infra.resource.v1;

import com.bankbox.infra.converter.BankConverter;
import com.bankbox.domain.entity.BankName;
import com.bankbox.infra.dto.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/banks")
public class BankResource {

	private final BankConverter bankConverter;

	public BankResource(BankConverter bankConverter) {
		this.bankConverter = bankConverter;
	}

	@GetMapping
	public ResponseEntity<List<BankResponse>> retrieveBanks(@RequestParam(required = false) String name) {
		List<BankName> banks = List.of(BankName.values());
		if (Objects.nonNull(name)) {
			banks = banks.stream()
				.filter(bank -> bank.getFormattedName().toLowerCase().startsWith(name.toLowerCase()))
				.collect(Collectors.toList());
		}

		return ResponseEntity.ok(bankConverter.toResponse(banks));
	}
}

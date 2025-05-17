package com.bankbox.infra.resource.v1;

import com.bankbox.domain.service.random.RandomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/generators")
public class RandomDataResource {

	private final RandomService randomService;

  public RandomDataResource(RandomService randomService) {
    this.randomService = randomService;
  }

  @PostMapping("/customers")
	public ResponseEntity<Void> generateRandomCustomers() {
		randomService.generateRandomCustomers();
		return ResponseEntity.ok().build();
	}
}

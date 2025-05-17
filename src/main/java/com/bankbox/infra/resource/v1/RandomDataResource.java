package com.bankbox.infra.resource.v1;

import com.bankbox.domain.entity.RandomConfiguration;
import com.bankbox.domain.entity.RandomSummary;
import com.bankbox.domain.service.random.RandomService;
import com.bankbox.infra.dto.RandomConfigurationRequest;
import com.bankbox.infra.dto.RandomSummaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/generators")
public class RandomDataResource {

	private final RandomService randomService;

  public RandomDataResource(RandomService randomService) {
    this.randomService = randomService;
  }

  @PostMapping("/customers")
	public ResponseEntity<RandomSummaryResponse> generateRandomCustomers(
		@Valid @RequestBody RandomConfigurationRequest configurationRequest
	) {
		RandomConfiguration configuration = RandomConfiguration.builder()
			.maximum(configurationRequest.maximum)
			.maximumPerFirstName(configurationRequest.maximumPerFirstName)
			.build();

		RandomSummary summary = randomService.generateRandomCustomers(configuration);
		return ResponseEntity.ok(new RandomSummaryResponse(summary.getTotalGenerated()));
	}
}

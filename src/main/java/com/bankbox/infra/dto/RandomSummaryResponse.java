package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RandomSummaryResponse {
  @JsonProperty("total_generated")
  public Integer totalGenerated;

  public RandomSummaryResponse(Integer totalGenerated) {
    this.totalGenerated = totalGenerated;
  }
}

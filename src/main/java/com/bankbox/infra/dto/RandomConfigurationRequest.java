package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RandomConfigurationRequest {
  @JsonProperty(value = "maximum", required = true)
  public Integer maximum;
  @JsonProperty(value = "maximum_per_first_name", required = true)
  public Integer maximumPerFirstName;
}

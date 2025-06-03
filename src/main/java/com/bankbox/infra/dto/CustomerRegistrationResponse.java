package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerRegistrationResponse {
  @JsonProperty("registration_id")
  public Long registrationId;
}

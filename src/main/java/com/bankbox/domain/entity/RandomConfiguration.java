package com.bankbox.domain.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RandomConfiguration {
  private Integer maximum;
  private Integer maximumPerFirstName;
}

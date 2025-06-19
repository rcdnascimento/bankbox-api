package com.bankbox.infra.dto;

import com.bankbox.domain.entity.InstallmentStatusEnum;

import java.math.BigDecimal;

public class CreditCardInstallmentResponse {
  public BigDecimal value;
  public InstallmentStatusEnum status;
  public Integer number;
}

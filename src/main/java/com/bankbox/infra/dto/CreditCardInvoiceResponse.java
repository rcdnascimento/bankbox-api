package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.YearMonth;

public class CreditCardInvoiceResponse {
  public YearMonth month;

  @JsonProperty(value = "due_date")
  public LocalDateTime dueDate;

  @JsonProperty(value = "paid_at")
  public LocalDateTime paidAt;
}

package com.bankbox.infra.dto;

import java.util.List;

public class PaginatedCustomerFullResponse {
  public List<CustomerFullResponse> customers;
  public int totalPages;
  public long totalElements;
  public int page;
  public int size;
}

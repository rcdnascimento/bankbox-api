package com.bankbox.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Consent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;
  @ManyToOne
  @JoinColumn(name = "bank_id", nullable = false)
  private Bank bank;
  @ManyToMany
  @JoinTable(
      name = "consent_roles",
      joinColumns = @JoinColumn(name = "consent_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private List<ConsentRole> roles;
}

package com.bankbox.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Consent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

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
  private List<ConsentRole> roles = new ArrayList<>();

  public void addRole(ConsentRole role) {
    if (roles != null) {
      roles.add(role);
    }
  }
}

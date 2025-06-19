package com.bankbox.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PixKey {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "`key`")
  private String key;

  @Enumerated(EnumType.STRING)
  private PixTypeEnum type;

  @JoinColumn(name = "bank_account_id")
  @ManyToOne
  private BankAccount bankAccount;

  public PixKey(String key, PixTypeEnum type, BankAccount bankAccount) {
    this.key = key;
    this.type = type;
    this.bankAccount = bankAccount;
  }
}

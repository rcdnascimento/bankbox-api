package com.bankbox.domain.service.creditcardtransaction;

import com.bankbox.domain.entity.CreditCardTransaction;

import java.util.List;

public interface RetrieveCreditCardTransaction {
  List<CreditCardTransaction> retrieveByCreditCard(Long creditCardId);
}

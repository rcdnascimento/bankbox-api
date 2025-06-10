package com.bankbox.domain.service.creditcardtransaction;

import com.bankbox.domain.entity.CreditCardTransaction;
import com.bankbox.infra.dto.CreditCardTransactionRequest;

public interface PersistCreditCardTransaction {
  CreditCardTransaction createTransaction(CreditCardTransactionRequest transactionRequest);
}

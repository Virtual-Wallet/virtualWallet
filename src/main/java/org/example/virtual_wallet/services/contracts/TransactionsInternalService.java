package org.example.virtual_wallet.services.contracts;


import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface TransactionsInternalService {
    TransactionsInternal create(TransactionsInternal transactionsInternal);

    List<TransactionsInternal> getAll();

    List<TransactionsInternal> getIncoming(User user);

    List<TransactionsInternal> getOutgoing(User user);
}

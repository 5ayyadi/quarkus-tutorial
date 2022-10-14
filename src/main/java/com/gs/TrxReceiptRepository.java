package com.gs;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.core.models.TransactionStatus;
import com.core.models.TrxReceipt;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TrxReceiptRepository implements PanacheRepository<TrxReceipt> {

    // public List<Trx> find
    public List<TrxReceipt> parsedTrxReceipts() {
        return list(("status"), TransactionStatus.PARSED);

    }

}

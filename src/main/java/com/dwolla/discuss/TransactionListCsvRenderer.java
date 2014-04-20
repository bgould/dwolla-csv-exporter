package com.dwolla.discuss;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionListCsvRenderer extends AbstractCsvRenderer<Transaction> {

    public TransactionListCsvRenderer() {
        super(Transaction.class);
    }

    @Override
    protected void configure() {
        addColumn("id", Integer.class);
        addColumn("amount", BigDecimal.class);
        addColumn("date", Date.class);
        addColumn("type", TransactionType.class);
        addColumn("userType", String.class);
        addColumn("destinationId", String.class);
        addColumn("destinationName", String.class);
        addColumn("sourceId", String.class);
        addColumn("sourceName", String.class);
        addColumn("clearingDate", Date.class);
        addColumn("status", Status.class);
        addColumn("notes", String.class);
        addColumn("originalTransactionId", String.class);
    }

}

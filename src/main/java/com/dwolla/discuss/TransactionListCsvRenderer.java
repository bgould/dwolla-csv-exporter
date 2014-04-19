package com.dwolla.discuss;

import java.math.BigDecimal;

public class TransactionListCsvRenderer extends AbstractCsvRenderer<Transaction> {

    public TransactionListCsvRenderer(Class<Transaction> rowType) {
        super(rowType);
    }

    @Override
    protected void configure() {
        addColumn("id", Integer.class);
        addColumn("amount", BigDecimal.class);
    }

}

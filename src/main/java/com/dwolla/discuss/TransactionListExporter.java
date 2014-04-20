package com.dwolla.discuss;

import java.util.Date;
import java.util.List;

public class TransactionListExporter {

    private String oauthToken;
    
    private int pageSize = 100;
    
    private Renderer<Transaction> renderer = new TransactionListCsvRenderer();
    
    public void setOauthToken(String token) {
        this.oauthToken = token;
    }
    
    public void export() {
        if (renderer != null) {
            renderer.begin();
        }
        final TransactionList transactionList = new TransactionList(oauthToken);
        transactionList.setSinceDate(new Date(0));
        transactionList.setEndDate(new Date());
        transactionList.setLimit(pageSize);
        List<Transaction> transactions = null;
        int i = 0;
        do {
            try {
                transactions = transactionList.request();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            transactionList.setSkip(transactionList.getSkip() + pageSize);
            for (Transaction transaction : transactions) {
                if (renderer != null) {
                    renderer.row(transaction);
                }
                i++;
            }
        } while (transactions.size() >= pageSize);
        if (renderer != null) {
            renderer.end();
        }
        System.err.println("Number of transactions: " + i);
    }
    
}

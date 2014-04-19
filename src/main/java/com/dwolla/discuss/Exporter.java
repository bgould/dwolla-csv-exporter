package com.dwolla.discuss;

import java.util.Date;
import java.util.List;

public class Exporter {

    private String oauthToken;
    
    private int pageSize = 33;
    
    private Renderer renderer;
    
    public static void main(String... args) throws Exception {
        final Exporter exporter = new Exporter();
        exporter.oauthToken = args[0];
        exporter.export();
    }
    
    public void export() throws Exception {
        
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
            transactions = transactionList.request();
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
        
        System.out.println("Number of transactions: " + i);
        
    }
    
}

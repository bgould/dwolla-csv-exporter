package com.dwolla.discuss;

import java.io.PrintWriter;

public abstract class AbstractCsvRenderer implements Renderer {

    private final PrintWriter out;
    
    private String separator;
    
    private boolean quoteValues;
    
    private boolean newlineAtEnd = true;
    
    public AbstractCsvRenderer() {
        this.out = new PrintWriter(System.out);
    }
    
    
    
}

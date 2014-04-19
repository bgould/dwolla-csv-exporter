package com.dwolla.discuss;

public interface Renderer {

    public void begin();
    
    public void row(Transaction t);
    
    public void end();
    
}

package com.dwolla.discuss;

public interface Renderer<T> {

    public void begin();
    
    public void row(T t);
    
    public void end();
    
}

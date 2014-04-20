package org.bgould.dwolla.exporter;

public interface Renderer<T> {

    public void begin();
    
    public void row(T t);
    
    public void end();
    
}

package org.bgould.dwolla.exporter;

import java.io.Serializable;
import java.math.BigDecimal;

public class Fee implements Serializable {

    private static final long serialVersionUID = 727350811204399030L;

    private String id;
    
    private BigDecimal amount;
    
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}

package org.bgould.dwolla.exporter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Transaction implements Serializable {

    private static final long serialVersionUID = -218525501171839057L;

    private Status status;
    
    private String originalTransactionId;
    
    private BigDecimal amount;
    
    private Date date;
    
    private String destinationId;
    
    private String destinationName;
    
    private String id;
    
    private String sourceId;
    
    private String sourceName;
    
    private TransactionType type;
    
    private String userType;
    
    private Date clearingDate;
    
    private String notes;
    
    private Fee[] fees;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOriginalTransactionId() {
        return originalTransactionId;
    }

    public void setOriginalTransactionId(String originalTransactionId) {
        this.originalTransactionId = originalTransactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getClearingDate() {
        return clearingDate;
    }

    public void setClearingDate(Date clearingDate) {
        this.clearingDate = clearingDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Fee[] getFees() {
        return fees;
    }

    public void setFees(Fee[] fees) {
        this.fees = fees;
    }
    
}

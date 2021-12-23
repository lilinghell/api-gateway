package com.hell.security.token;

public class TokenImpl implements Token {
    private String uniqueId;
    private long accessDate;
    public TokenImpl() {
    }

    public TokenImpl(String uniqueId, long accessDate) {
        this.uniqueId = uniqueId;
        this.accessDate = accessDate;
    }

    public String toString() {
        return this.getClass().getName() + " :" + this.uniqueId + " " + this.accessDate;
    }

    public long getAccessDate() {
        return this.accessDate;
    }

    public void setAccessDate(long accessDate) {
        this.accessDate = accessDate;
    }

    public String getUniqueId() {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}

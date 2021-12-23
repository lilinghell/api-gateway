package com.hell.security.token;

import java.io.Serializable;

public interface Token extends Serializable {
    void setAccessDate(long var1);

    long getAccessDate();

    void setUniqueId(String var1);

    String getUniqueId();
}

package com.hell.db.table.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class PSmsAuthPK implements Serializable {
    private String apiPath;
    private String mobilePhone;
}

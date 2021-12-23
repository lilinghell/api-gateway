package com.hell.entity.api;

import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiExt;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiInfo implements Serializable {
    private SApi api;
    private SApiExt apiExt;
    private Integer apiGroupKey;
}

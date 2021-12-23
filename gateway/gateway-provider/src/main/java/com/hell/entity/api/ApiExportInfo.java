package com.hell.entity.api;

import com.hell.db.table.provider.SApiGroup;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ApiExportInfo implements Serializable {
    private List<ApiInfo> apiInfoList;
    private List<SApiGroup> apiGroupList;
}

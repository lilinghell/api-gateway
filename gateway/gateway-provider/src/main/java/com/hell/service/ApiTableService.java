package com.hell.service;

import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SAppEnv;
import com.hell.entity.api.ApiTable;
import com.hell.core.exception.ValidationException;

import java.io.Serializable;
import java.util.List;

public interface ApiTableService extends Serializable {
    List<ApiTable> getApiTableList(SApi api, SAppEnv appEnv) throws ValidationException;
}

package com.hell.dto.response;

import com.hell.db.table.provider.SApiExt;
import com.hell.config.response.Response;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiExtResponse extends SApiExt implements Serializable, Response {
    private String syncGateway;
}

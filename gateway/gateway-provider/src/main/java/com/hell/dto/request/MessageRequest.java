package com.hell.dto.request;


import com.hell.config.request.BaseRequest;
import lombok.Data;

@Data
public class MessageRequest extends BaseRequest {
    private String type;
    private String status;
    private Integer seq;
}

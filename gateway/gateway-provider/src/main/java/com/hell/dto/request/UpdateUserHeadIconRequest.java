package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class UpdateUserHeadIconRequest extends BaseRequest {
    @NotNull
    private Integer userSeq;

    private MultipartFile file;
}

package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class ReEntInfoRequest extends BaseRequest {

    @NotBlank
    private String entName;
    @NotBlank
    private String entShortName;
    @NotBlank
    private String corpName;
    @NotBlank
    private String corpPhone;
    @NotBlank
    private String corpIdType;
    @NotBlank
    private String corpIdNo;

    private String contactName;
    private String contactPhone;

    private String entDec;
    private String entShortDec;
    @NotBlank
    private String entCode;
    @NotBlank
    private String registerAddr;
    private MultipartFile entLicenseFile;
    private MultipartFile corpFile0;
    private MultipartFile corpFile1;
}

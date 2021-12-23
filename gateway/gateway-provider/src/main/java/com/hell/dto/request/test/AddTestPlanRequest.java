package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddTestPlanRequest extends BaseRequest {
    @NotNull
    private Integer appSeq;
    @NotBlank
    private String name;
    @NotNull
    private List<String> planTime;
    @NotNull
    private List<String> planRule;
    @NotNull
    private Boolean usingFlg;
    private String planInfo;
}

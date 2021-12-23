package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class DeptRequest extends BaseRequest {

    private Integer seq;
    @NotBlank
    @Length(max = 30,min = 1 ,message = "部门名长度最大不超30")
    private String deptName;
    private String info;
    @NotBlank
    private String status;
    private Integer parentSeq;
}

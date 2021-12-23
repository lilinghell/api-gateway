package com.hell.dto.request.tag;

import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddServiceTagRequest extends BaseRequest {
    @NotBlank
    @Length(min = 0 , max = 20)
    private String name;
    @NotNull
    private Integer appSeq;
    private String color;
}

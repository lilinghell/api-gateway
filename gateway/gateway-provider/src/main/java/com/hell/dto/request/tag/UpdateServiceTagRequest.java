package com.hell.dto.request.tag;

import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateServiceTagRequest extends BaseRequest {
    @NotNull
    private Integer seq;
    @NotBlank
    @Length(max = 20 , min = 1 ,message = "长度最大不超20")
    private String name;
}

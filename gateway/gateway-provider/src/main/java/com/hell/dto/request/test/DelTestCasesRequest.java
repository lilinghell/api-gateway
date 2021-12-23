package com.hell.dto.request.test;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class DelTestCasesRequest {
    @NotNull
    private Integer seq;
}

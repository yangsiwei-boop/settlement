package com.studio.settlement.bean.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseDTO {

    @Schema(description = "页码")
    private Integer pageNo;

    @Schema(description = "每页条数")
    private Integer pageSize;
}

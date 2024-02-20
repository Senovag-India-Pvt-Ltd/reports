package com.sericulture.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ReelerTxnRequest extends RequestBody {

    @Schema(name = "godownId", example = "1")
    private int godownId;

    @Schema(name = "marketId", example = "1")
    private int marketId;

    @Schema(name = "reportFromDate")
    private LocalDate reportFromDate;

    @Schema(name = "reportToDate")
    private LocalDate reportToDate;

    @Schema(name = "reelerNumber", example = "1")
    private String reelerNumber;

}
package com.sericulture.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ReelerBiddingReportRequest implements Serializable {

    @Schema(name = "godownId", example = "1")
    private int godownId;

    @Schema(name = "marketId", example = "1")
    private int marketId;

    @Schema(name = "reportFromDate")
    private LocalDate reportFromDate;

    @Schema(name = "reelerNumber")
    private String reelerNumber;
}
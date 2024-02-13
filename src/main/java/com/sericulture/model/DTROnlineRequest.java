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
public class DTROnlineRequest implements Serializable {

    @Schema(name = "reelerId", example = "1")
    private int reelerId;

    @Schema(name = "marketId", example = "1")
    private int marketId;

    @Schema(name = "fromDate")
    private LocalDate fromDate;

    @Schema(name = "toDate")
    private LocalDate toDate;
}
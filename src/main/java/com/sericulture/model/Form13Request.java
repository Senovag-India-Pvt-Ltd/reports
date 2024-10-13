package com.sericulture.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Form13Request extends RequestBody{

    @Schema(name = "godownId", example = "1")
    private int godownId;

    @Schema(name = "marketId", example = "1")
    private int marketId;

    @Schema(name = "fromDate")
    private LocalDate fromDate;

    @Schema(name = "toDate")
    private LocalDate toDate;

    @Schema(name = "districtId")
    private Long districtId;

    @Schema(name = "raceId")
    private int raceId;


//    private LocalDate auctionDate;
//    private LocalDate fromDate;
//    private LocalDate toDate;
//    Long districtId;
}
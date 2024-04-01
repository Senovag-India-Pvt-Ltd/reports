package com.sericulture.model.MarketReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketWiseInfo {
    private String marketName;
    private List<MarketReportRaceWise> marketReportRaceWises;
    private String totalWeightStarting;
    private String totalWeightEnding;
    private String totalAmountStarting;
    private String totalAmountEnding;
    private String avgAmountStarting;
    private String avgAmountEnding;
    private String marketFeeStarting;
    private String marketFeeEnding;
    private String lotsStarting;
    private String lotsEnding;
}
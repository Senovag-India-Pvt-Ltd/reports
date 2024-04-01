package com.sericulture.model.MarketReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketReportInfo {
    private String startingWeight;
    private String startingAmount;
    private String startingAvg;
    private String endingWeight;
    private String endingAmount;
    private String endingAvg;
}
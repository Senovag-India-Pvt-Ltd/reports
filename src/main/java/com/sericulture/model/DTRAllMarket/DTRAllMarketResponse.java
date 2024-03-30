package com.sericulture.model.DTRAllMarket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DTRAllMarketResponse {
    private DTRInfoResponse content;

    private String finalDiff = "";
    private String totalWeight = "";
    private String totalMin = "";
    private String totalMax = "";
    private String totalAvg = "";
    private String lastTotalWeight = "";
    private String lastMin = "";
    private String lastMax = "";
    private String lastAvg = "";

    private String raceNameInKannada1 = "";
    private String weight1 = "";
    private String minAmount1 = "";
    private String maxAmount1 = "";
    private String avgAmount1 = "";
    private String lastWeight1 = "";
    private String lastMinAmount1 = "";
    private String lastMaxAmount1 = "";
    private String lastAvgAmount1 = "";

    private String thisYearWeight="";
    private String prevYearWeight="";
    private String prevYearAmount="";
    private String thisYearAmount="";
    private String weightMonthDiff="";

    private String header1 = "";
    private String year1 = "";
    private String year2 = "";
    private String logurl="";
}
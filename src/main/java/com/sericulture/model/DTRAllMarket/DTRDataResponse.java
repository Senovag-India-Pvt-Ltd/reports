package com.sericulture.model.DTRAllMarket;

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
public class DTRDataResponse {
    private List<DTRMarketResponse> dtrMarketResponses;
    private DTRResponse sumOfToday;
    private DTRResponse sumOfPreviousYear;
    private String totalWeightDiff;
    private List<DTRResponse> raceByToday;
    private List<DTRResponse> raceByPrevYear;

    private String thisYearWeight;
    private String prevYearWeight;
    private String prevYearAmount;
    private String thisYearAmount;
}
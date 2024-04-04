package com.sericulture.model.MarketWiseReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.MarketReport.MarketWiseInfo;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivisionReport {
    private String divisionName;
    private List<MarketWiseInfo> marketWiseInfoList;
    private List<MarketWiseInfo> divisionWiseSum;
}
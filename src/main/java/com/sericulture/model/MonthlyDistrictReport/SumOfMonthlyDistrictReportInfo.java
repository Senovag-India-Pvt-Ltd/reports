package com.sericulture.model.MonthlyDistrictReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SumOfMonthlyDistrictReportInfo {
    private String raceName;
    private String totalLots;
    private String totalWeight;
}
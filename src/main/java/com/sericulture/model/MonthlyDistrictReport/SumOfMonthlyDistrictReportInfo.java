package com.sericulture.model.MonthlyDistrictReport;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SumOfMonthlyDistrictReportInfo {
    private String raceName;
    private String totalLots;
    private String totalWeight;
}
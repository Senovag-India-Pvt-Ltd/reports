package com.sericulture.model.MonthlyDistrictReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.ResponseBody;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyDistrictReport extends ResponseBody {
    private String startDate;
    private String endDate;
    private String marketNameInKannada;
    List<MonthlyDistrictReportInfo> monthlyDistrictReportInfoList;
    List<SumOfMonthlyDistrictReportInfo> sumOfMonthlyDistrictReportInfoList;
}
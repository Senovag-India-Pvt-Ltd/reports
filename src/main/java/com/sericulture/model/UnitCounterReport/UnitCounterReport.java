package com.sericulture.model.UnitCounterReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.MonthlyDistrictReport.MonthlyDistrictReportInfo;
import com.sericulture.model.MonthlyDistrictReport.SumOfMonthlyDistrictReportInfo;
import com.sericulture.model.ResponseBody;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitCounterReport extends ResponseBody {
    private LocalDate reportFromDate;
    List<UnitCounterReportInfo> unitCounterReportInfoList;
//    List<SumOfMonthlyDistrictReportInfo> sumOfMonthlyDistrictReportInfoList;
}

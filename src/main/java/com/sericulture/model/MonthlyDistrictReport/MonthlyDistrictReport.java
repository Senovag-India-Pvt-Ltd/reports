package com.sericulture.model.MonthlyDistrictReport;

import com.sericulture.model.ResponseBody;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class MonthlyDistrictReport extends ResponseBody {
    private String startDate;
    private String endDate;
    private String marketName;
    List<MonthlyDistrictReportInfo> monthlyDistrictReportInfoList;
    List<SumOfMonthlyDistrictReportInfo> sumOfMonthlyDistrictReportInfoList;
}
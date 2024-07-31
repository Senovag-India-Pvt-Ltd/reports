package com.sericulture.model.UnitCounterReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.MonthlyDistrictReport.MonthlyDistrictReport;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitCounterReportResponse {
//    private UnitCounterReport content;

    private List<UnitCounterReportInfo> content;
}

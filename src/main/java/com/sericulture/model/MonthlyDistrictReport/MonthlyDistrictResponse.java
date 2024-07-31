package com.sericulture.model.MonthlyDistrictReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.MonthlyReport.MonthlyReportRaceWise;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyDistrictResponse {
    private MonthlyDistrictReport content;
}

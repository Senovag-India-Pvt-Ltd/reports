package com.sericulture.model.MonthlyReport;

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
public class MonthlyReportResponse {
    private List<MonthlyReportRaceWise> monthlyReportRaceWiseList;
}
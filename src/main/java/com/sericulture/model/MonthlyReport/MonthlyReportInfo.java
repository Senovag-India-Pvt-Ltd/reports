package com.sericulture.model.MonthlyReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyReportInfo {
    private String startWeight;
    private String endWeight;
    private String startAmount;
    private String endAmount;
    private String startAvg;
    private String endAvg;
}
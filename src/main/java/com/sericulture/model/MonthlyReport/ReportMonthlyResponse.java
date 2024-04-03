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
public class ReportMonthlyResponse {
    private MonthlyReport content;
}

package com.sericulture.model.MonthlyReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.RequestBody;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyReportRequest extends RequestBody {
    private LocalDate startDate;
    private LocalDate endDate;
}
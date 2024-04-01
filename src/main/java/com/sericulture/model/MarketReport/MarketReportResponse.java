package com.sericulture.model.MarketReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketReportResponse {
    private MarketReports marketReports;
}
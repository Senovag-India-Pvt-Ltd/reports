package com.sericulture.model.MonthlyDistrictReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyDistrictReportInfo {
    private String serialNumber;
    private String districtName;
    private String talukName;
    private String stateName;
    private String totalLots;
    private String totalWeight;
    private String raceName;
}


package com.sericulture.model.VahivaatuReport;

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
public class DistrictWise {
    private String districtName;
    private List<RaceWiseReport> raceWiseReports;
}
package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.UnitCounterReport.UnitCounterReportInfo;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReelerMFResponse {
    private List<ReelerMFReportInfo> content;

}

package com.sericulture.model;

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
public class BreakdownLotStatus {
    private String description;
    private String lot;
    private String weight;
    private String percentage;

    private String headerText;
    private String averageRateText;
    List<BreakdownLotStatus> lotsFrom201to300;
}
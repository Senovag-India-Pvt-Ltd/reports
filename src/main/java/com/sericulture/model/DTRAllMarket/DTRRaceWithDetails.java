package com.sericulture.model.DTRAllMarket;

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
public class DTRRaceWithDetails {
    private String marketNameInKannada="";
    private String raceNameInKannada="";
    private String weight="";
    private String minAmount="";
    private String maxAmount="";
    private String avgAmount="";

    private String lastDiff="";
    private String lastWeight="";
    private String lastMinAmount="";
    private String lastMaxAmount="";
    private String lastAvgAmount="";
    private String prevWeight = "";
    private String prevAvg = "";
}
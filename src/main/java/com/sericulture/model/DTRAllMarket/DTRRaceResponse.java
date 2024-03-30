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
public class DTRRaceResponse {
    private String raceNameInKannada;
    private List<DTRResponse> dtrResponses;
    private List<DTRResponse> prevResponses;
    private List<DTRResponse> lastYearResponses;
}
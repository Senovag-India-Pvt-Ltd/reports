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
public class DTRMarketResponse {
    private String marketNameInKannada;
    private List<DTRRaceResponse> dtrRaceResponses;
}
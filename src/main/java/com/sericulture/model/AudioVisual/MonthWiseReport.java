package com.sericulture.model.AudioVisual;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.DTRAllMarket.DTRMarketResponse;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthWiseReport {
    private String month;
    private List<DTRMarketResponse> dtrMarketResponses;
}
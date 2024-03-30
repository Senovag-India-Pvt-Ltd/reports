package com.sericulture.model.DTRAllMarket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DTRResponse {
    private String raceName;
    private String weight;
    private String minAmount;
    private String maxAmount;
    private String avgAmount;
}
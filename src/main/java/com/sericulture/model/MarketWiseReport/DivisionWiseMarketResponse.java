package com.sericulture.model.MarketWiseReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivisionWiseMarketResponse {
    private DivisionWiseReport divisionWiseReport;
}
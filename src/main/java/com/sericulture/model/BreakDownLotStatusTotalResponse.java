package com.sericulture.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class BreakDownLotStatusTotalResponse extends ResponseBody {
    private String description;
    private String totalLots;
    private String totalWeight;
    private String totalPercentage;
}

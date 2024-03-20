package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupLotStatus {
    private String description;
    private String lot;
    private String weight;
    private String amount;
    private String mf;
    private String min;
    private String max;
    private String avg;

    private String stateName = "";
    private String max21 = "";
    private String min21= "";
    private String avg21= "";
    private String amount21= "";
    private String weight21= "";
    private String lot21= "";
    private String mf21= "";

    private String raceName = "";
    private String max31 = "";
    private String min31= "";
    private String avg31= "";
    private String amount31= "";
    private String weight31= "";
    private String lot31= "";
    private String mf31= "";
}
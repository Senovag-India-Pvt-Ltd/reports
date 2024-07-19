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

    private String totalLots;
    private String totalWeight;
    private String totalAmount;
    private String totalMarketFee;
    private String totalMin;
    private String totalMax;
    private String totalAvg;

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

    private String gender = "";
    private String max41 = "";
    private String min41= "";
    private String avg41= "";
    private String amount41= "";
    private String weight41= "";
    private String lot41= "";
    private String mf41= "";

    private String totalStateLots= "";
    private String totalStateWeight = "";
    private String totalStateAmount= "";
    private String totalStateMarketFee= "";
    private String totalStateMin= "";
    private String totalStateMax= "";
    private String totalStateAvg= "";

    private String totalGenderLots= "";
    private String totalGenderWeight = "";
    private String totalGenderAmount= "";
    private String totalGenderMarketFee= "";
    private String totalGenderMin= "";
    private String totalGenderMax= "";
    private String totalGenderAvg= "";

    private String totalRaceLots= "";
    private String totalRaceWeight = "";
    private String totalRaceAmount= "";
    private String totalRaceMarketFee= "";
    private String totalRaceMin= "";
    private String totalRaceMax= "";
    private String totalRaceAvg= "";
}
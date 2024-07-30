package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Form13ReportResponse {
    private Form13Response content;

    private String headerText = "";
    private String averageRate = "";

    private String lot1 = "";
    private String lot2= "";
    private String lot3= "";
    private String lot4= "";
    private String lot5= "";
    private String lot6= "";
    private String lot7= "";
    private String totalLot1= "";

    private String weight1="";
    private String weight2="";
    private String weight3="";
    private String weight4="";
    private String weight5="";
    private String weight6="";
    private String weight7="";
    private String totalWeight1="";

    private String perc1="";
    private String perc2="";
    private String perc3="";
    private String perc4="";
    private String perc5="";
    private String perc6="";
    private String perc7="";

    private String lot11 = "";
    private String weight11 = "";
    private String amount11= "";
    private String mf11= "";
    private String min11= "";
    private String max11= "";
    private String avg11= "";

    private String stateName = "";
    private String max21 = "";
    private String min21= "";
    private String avg21= "";
    private String amount21= "";
    private String weight21= "";
    private String lot21= "";
    private String mf21= "";

    private String lot41 = "";
    private String lot42= "";
    private String lot43= "";
    private String lot44= "";
    private String lot45= "";
    private String lot46= "";
    private String lot47= "";

    private String weight41="";
    private String weight42="";
    private String weight43="";
    private String weight44="";
    private String weight45="";
    private String weight46="";
    private String weight47="";

    private String perc41="";
    private String perc42="";
    private String perc43="";
    private String perc44="";
    private String perc45="";
    private String perc46="";
    private String perc47="";

    private String averageDesc1="";
    private String averageDesc2="";
    private String lot51="";
    private String lot52="";
    private String weight51="";
    private String weight52="";
    private String perc51="";
    private String perc52="";


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

    private String description = "";
    private String totalLots= "";
    private String totalWeight = "";
    private String totalPercentage = "";

    private String description2 = "";
    private String totalLots2= "";
    private String totalWeight2 = "";
    private String totalPercentage2 = "";


}
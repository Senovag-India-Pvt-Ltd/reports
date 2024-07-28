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
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DTROnlineReportResponse extends ResponseBody {
    private List<DTROnlineReportUnitDetail> dtrOnlineReportUnitDetailList = new ArrayList<>();
    private int totalLots;
    private int paymentSuccessLots;
    private double totalFarmerAmount;
    private double totalReelerAmount;
    private double totalReelerMarketFee;
    private double totalFarmerMarketFee;

    private int totalBidAmount;
    private float totalWeight;
    private float totallotSoldOutAmount;
    private String marketNameKannada;
    private String farmerTaluk;
    private String farmerVillage;
}
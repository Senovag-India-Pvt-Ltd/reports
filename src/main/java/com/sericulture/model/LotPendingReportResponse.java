package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class LotPendingReportResponse {
    private String farmerNumber;
    private String farmerFirstName;
    private String farmerMiddleName;
    private String farmerLastName;
    private String farmerAddress;
    private String farmerTaluk;
    private String farmerVillage;
    private String ifscCode;
    private String accountNumber;
    private String reelerLicense;
    private String reelerName;
    private String reelerAddress;
    private String allottedLotId;
    private String auctionDate;
    private float lotWeight;
    private double farmerMarketFee;
    private double reelerMarketFee;
    private float lotSoldOutAmount;
    private float bidAmount;
    private double reelerCurrentBalance;
    private int farmerEstimatedWeight;
    private List<Float> lotWeightDetail;
    private String marketName;
    private String race;
    private String source;
    private float tareWeight;
    private String serialNumber;
    private BigDecimal marketAuctionId;
    private String marketNameKannada;
    private String farmerNameKannada;
    private Date auctionDateWithTime;
    private String farmerMobileNumber;
    private String reelerMobileNumber;
    private String reelerNumber;
    private String reelerNameKannada;
    private double farmerAmount;
    private double reelerAmount;
    private List<Integer> smallBinList;
    private List<Integer> bigBinList;
    private String loginName;
    private long serailNumberForPagination;
    private String acceptedBy;

    private String headerText;
    private String farmerDetails;
    private String marketFee;
    private String reeler_amount;
    private String bankDetails;
    private String accpetedBy;
    private String shed;
    private String weight;
    private String reelerDetails;
}
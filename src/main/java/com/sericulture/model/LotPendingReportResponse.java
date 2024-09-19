package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@JsonFormat
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
    private String farmerTalukInKannada;
    private String farmerVillageInKannada;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private String auctionDateWithTime;
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
    private String village;

}
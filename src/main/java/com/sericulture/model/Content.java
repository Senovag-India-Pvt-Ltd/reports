package com.sericulture.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder

public class Content {
    private String farmerNumber;
    private String serialNumber;

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

    private String lotWeight;

    private double farmerMarketFee;

    private double reelerMarketFee;

    private String lotSoldOutAmount;

    private String bidAmount;

    private double reelerCurrentBalance;

    private int farmerEstimatedWeight;

    private List<Float> lotWeightDetail;

    private String logurl;

    private String feespaid;
    private String amountfarmer;
    private String amountrealar;
    private String marketName;
    private String race;
    private String source;

}

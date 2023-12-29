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

    private int allottedLotId;

    private String auctionDate;

    private float lotWeight;

    private double farmerMarketFee;

    private double reelerMarketFee;

    private float lotSoldOutAmount;

    private float bidAmount;

    private double reelerCurrentBalance;

    private int farmerEstimatedWeight;

    private List<Float> lotWeightDetail;

    private String logurl;
}

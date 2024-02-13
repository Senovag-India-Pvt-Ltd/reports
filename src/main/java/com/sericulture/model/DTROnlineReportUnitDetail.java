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
public class DTROnlineReportUnitDetail {
    private int serialNumber;
    private int allottedLotId;
    private String farmerFirstName;
    private String farmerMiddleName;
    private String farmerLastName;
    private String farmerNumber;
    private String farmerMobileNumber;
    private float weight;
    private int bidAmount;
    private double farmerAmount;
    private double reelerAmount;
    private float lotSoldOutAmount;
    private double farmerMarketFee;
    private double reelerMarketFee;
    private String reelerLicense;
    private String reelerName;
    private String reelerMobile;
    private String bankName;
    private String branchName;
    private String ifscCode;
    private String accountNumber;

    private String farmerDetails;
    private String reelerDetails;
    private String bankDetails;
    private double marketFee;
    private String headerText;
    private String farmerAddress;
    private String marketNameKannada;
}
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
    private String serialNumber;
    private String allottedLotId;
    private String farmerFirstName;
    private String farmerMiddleName;
    private String farmerLastName;
    private String farmerNumber;
    private String farmerMobileNumber;
    private String weight;
    private String bidAmount;
    private String farmerAmount;
    private String reelerAmount;
    private String lotSoldOutAmount;
    private String farmerMarketFee;
    private String reelerMarketFee;
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
    private String marketFee;
    private String headerText;
    private String farmerAddress;
    private String marketNameKannada;

    private String reeler_transaction_amt;
    private String mf_amount;
    private String farmer_cheque;
    private String total_lots;
    private String total_weight_with_amount_details;
    private String transacted_lots;
    private String not_transacted_lots;
    private String farmerTaluk;
    private String farmerVillage;
    private String raceName;
    private Long maxAmount;
    private Long minAmount;
    private float avgAmount;
    private String max_amount;
    private String min_amount;
    private String avg_amount;
}
package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FarmerTxnInfo {
    private int serialNumber;
    private int allottedLotId;
    private String lotTransactionDate;
    private float lotSoldOutAmount;
    private double farmerMarketFee;
    private double farmerAmount;
    private float weight;
    private int bidAmount;
    private String breed;

    private String farmerFirstName;
    private String farmerMiddleName;
    private String farmerLastName;
    private String farmerNumber;
    private double totalSaleAmount;
    private double totalMarketFee;
    private double totalFarmerAmount;
    private String headerText;
    private String farmer_details_farmer_transaction;
    private String total_sale_amount_farmer_transaction;
    private String total_market_fee_farmer_transaction;
    private String total_amount_farmer_transaction;
    private String farmerDetails;
    private String marketFee;
    private String reelerAmount;
    private String reelerDetails;
    private String bankDetails;
    private String ifscCode;
    private String accountNumber;

}
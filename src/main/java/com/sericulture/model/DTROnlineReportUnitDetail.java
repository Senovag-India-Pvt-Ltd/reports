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
    private String traderMarketFee;
    private String farmerMarketFee;
    private String reelerMarketFee;
    private String reelerLicense;
    private String reelerName;
    private String reelerMobile;
    private String reelerAddress;
    private String reelerBankName;
    private String reelerBranchName;
    private String reelerIfscCode;
    private String reelerNumber;
    private String traderFirstName;
    private String traderMiddleName;
    private String traderLastName;
    private String traderFatherName;
    private String traderAddress;
    private String traderSilkType;
    private float traderLicenseFee;
    private String traderMobileNumber;
    private String traderArnNumber;
    private String traderLicenseNumber;
    private String traderStateNameInKannada;
    private String traderDistrictNameInKannada;
    private String traderApplicationNumber;
    private String traderLicenseChallanNumber;
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
    private String cocoonAge;
    private String farmerNameKannada;
    private String fatherNameKannada;
    private String talukNameInKannada;
    private String villageNameInKannada;
    private Long maxAmount;
    private Long minAmount;
    private float avgAmount;
    private String max_amount;
    private String min_amount;
    private String avg_amount;
    private String marketNameInKannada;
    private String reelerVillage;
    private String reelerTaluk;
    private String reelerBankAccountNumber;
}
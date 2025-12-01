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

public class AcknowledgementReceiptResponse {
    private String header;
    private String header1;
    private String header2;
    private String header3;
    private String header4;
    private String header5;
    private String reelingShedSqft;


    private String acceptedDate;
    private String date;
    private String farmerFirstName;
    private String addressText;
    private String districtName;
    private String talukName;
    private String hobliName;
    private String villageName;
    private String fruitsId;
    private String lineItemComment;
    private String financialYear;
    private String schemeNameInKannada;
    private String subSchemeNameInKannada;
    private String fatherNameKan;
    private String mobileNumber;
    private String arn;
    private String logurl;
    private String districtNameInKannada;
    private String hobliNameInKannada;
    private String villageNameInKannada;
    private String schemeId;
    private String surveyNumber;
    private String vendorName;
    private String vendorBankName;
    private String vendorIfscCode;
    private String vendorBranch;
    private String vendorAccountNumber;
    private String vendorUPI;
    private String talukNameInKannada;
    private String tscName;
    private String  loggedinUserDistrictName;
    private String loggedinUserTalukName;
    private String loggedinUserTscName;
    private Float unitPrice;
    private Float unitCost;
    private Float subsidyAmount;
    private Float schemeAmount;

    private String lotNo;
    private String noOfDfls;
    private String reelerNumber;
    private String reelingLicenseNumber;
    private String componentName;
    private String machineTypeName;
    private String categoryName;
    private String userName;
    private Long userMasterId;
    private String reelerName;

    private Float quantityOfCocoonsProduced;
    private String numberOfBasins;

}

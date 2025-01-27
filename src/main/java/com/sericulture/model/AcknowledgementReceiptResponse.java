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
}

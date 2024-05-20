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
public class SelectionLetterReportResponse {
    private String date;
    private String farmerFirstName;
    private String farmerLastName;
    private String farmerNumber;
    private String header4;
    private String header1;
    private String header2;
    private String header3;
    private String header5;
    private String header6;
    private String header7;
    private String fruitsId;
    private String farmerAddressText;
    private String districtName;
    private String talukName;
    private String hobliName;
    private String villageName;
    private String lineItemComment;
    private Float cost;
    private String vendorName;
    private String vendorAccountNumber;
    private String vendorBankName;
    private String vendorBankIfsc;
    private String vendorBranchName;
    private String vendorUpi;
    private String sanctionNo;
    private String financialYear;
    private String schemeNameInKannada;
    private String subSchemeNameInKannada;
    private String fatherNameKan;
    private String mobileNumber;
    private String scComponentName;

}

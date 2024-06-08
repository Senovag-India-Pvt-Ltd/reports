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
public class SanctionOrderResponse {
    private String header1;
    private String header2;
    private String header3;
    private String header4;
    private String header5;
    private String header6;
    private String header7;
    private String header8;
    private String header9;
    private String header10;
    private String header11;
    private String header12;
    private String header13;
    private String header14;
    private String header15;
    private String header16;
    private String header17;
    private String header18;
    private String header19;
    private String header20;
    private String header21;
    private String header22;
    private String header23;
    private String header24;
    private String header25;
    private String header26;
    private String header27;
    private String date;
    private String farmerFirstName;
    private String farmerNumber;
    private String fruitsId;
    private String farmerAddressText;
    private String districtName;
    private String talukName;
    private String hobliName;
    private String villageName;
    private String farmerAccountNumber;
    private String farmerBankName;
    private String farmerBankIfsc;
    private String farmerBranchName;
    private String lineItemComment;
    private Float cost;
    private String vendorName;
    private String vendorAccountNumber;
    private String vendorBankName;
    private String vendorBankIfsc;
    private String vendorBranchName;
    private String vendorUpi;
    private String financialYear;
    private String sanctionNo;
    private String schemeNameInKannada;
    private String subSchemeNameInKannada;
    private String fatherNameKan;
    private String mobileNumber;
    private String scComponentName;
}

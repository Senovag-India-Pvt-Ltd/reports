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

public class WorkOrderGenerationReportResponse {
    private String header1;
    private String header2;
    private String header3;
    private String header4;
    private String header5;
    private String header7;
    private String header8;
    private String header9;
    private String header10;
    private String header11;
    private String header12;

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
    private String header6;
    private Float cost;
    private String vendorName;
    private String vendorAccountNumber;
    private String vendorBankName;
    private String vendorBankIfsc;
    private String vendorBranchName;
    private String vendorUpi;
    private String workOrderId;
    private String financialYear;
    private String schemeNameInKannada;
    private String subSchemeNameInKannada;
    private String fatherNameKan;
    private String mobileNumber;
    private String scComponentName;
    private String logurl;
    private Float sanctionAmount;
    private Float schemeAmount;
    private String nameKan;
    private String tscNameInKannada;
    private String createdDate;
    private String acre;
    private String surveyNumber;
    private String arn;
    private String gunta;
    private String devAcre;
    private String devGunta;
    private String devFGunta;
    private String scApplicationFormId;
    private String schemeName;
    private String subSchemeName;
    private String workOrderNumber;
    private String userDistrictName;
    private String userTaluk;
    private String userTscName;
    private String createdTime;
    private String categoryName;



}

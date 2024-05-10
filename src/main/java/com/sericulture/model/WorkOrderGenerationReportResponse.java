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
}

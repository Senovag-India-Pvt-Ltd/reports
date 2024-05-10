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

}

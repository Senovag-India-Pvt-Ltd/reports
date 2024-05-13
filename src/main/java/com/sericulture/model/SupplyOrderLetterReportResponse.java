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
public class SupplyOrderLetterReportResponse {
    private String header1;
    private String header2;
    private String header3;
    private String header4;
    private String header5;
    private String header6;
    private String header7;
    private String header8;
    private String date;
    private String farmerFirstName;
    private String addressText;
    private String districtName;
    private String talukName;
    private String hobliName;
    private String villageName;
    private String fruitsId;
    private Float cost;
    private String lineItemComment;
    private String financialYear;
    private String schemeName;

}

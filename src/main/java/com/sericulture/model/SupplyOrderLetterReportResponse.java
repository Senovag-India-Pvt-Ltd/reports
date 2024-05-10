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
    private String date;
    private String farmerFirstName;
    private String farmerLastName;
    private String addressText;
    private String districtName;
    private String talukName;
    private String hobliName;
    private String villageName;
    private String fruitsId;
    private String lineItemComment;
    private String financialYear;
    private String schemeName;

}

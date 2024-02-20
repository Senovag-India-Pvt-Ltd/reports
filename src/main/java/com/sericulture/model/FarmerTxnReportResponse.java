package com.sericulture.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class FarmerTxnReportResponse extends ResponseBody {
    private String farmerFirstName;
    private String farmerMiddleName;
    private String farmerLastName;
    private String farmerNumber;
    private double totalSaleAmount;
    private double totalMarketFee;
    private double totalFarmerAmount;
    List<FarmerTxnInfo> farmerTxnInfoList;

    private String headerText;

}
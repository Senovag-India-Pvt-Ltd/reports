package com.sericulture.model.UnitCounterReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitCounterReportInfo {
    private String serialNumber;
    private int allottedLotId;
    private String lotTransactionDate;
    private String reelerLicense;
    private String reelerName;
    private String weight;
    private float lotSoldOutAmount;
    private int bidAmount;
    private double farmerMarketFee;
    private double reelerMarketFee;
}

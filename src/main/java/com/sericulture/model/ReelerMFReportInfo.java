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
public class ReelerMFReportInfo {
    private int serialNumber;
    private int allottedLotId;
    private String lotTransactionDate;
    private String reelerLicense;
    private String reelerName;
    private int bidAmount;
    private String weight;
    private float lotSoldOutAmount;
    private double farmerMarketFee;
    private double reelerMarketFee;
}

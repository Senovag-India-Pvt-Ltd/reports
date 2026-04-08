package com.sericulture.model;
import lombok.Data;

@Data
public class Buyer {
    private String lgBuyerType;
    private String lgBuyerName;
    private String lgLotWeight;
    private String lgAmount;
    private Long noOfCocoonPerKg;
    private String lgSoldOutAmount;
    private Double farmerAmount;
    private String remainingCocoon;
    private String lgMarketFee;
}

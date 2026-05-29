package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentItemResponse {

    // DBT equipment fields (deserialized from e-prefixed JSON keys)
    @JsonProperty("edescription")
    private String description;

    @JsonProperty("el1Rate")
    private String l1Rate;

    @JsonProperty("emachineQuantity")
    private Float machineQuantity;

    @JsonProperty("etaxInvoiceNo")
    private String taxInvoiceNo;

    @JsonProperty("etaxInvoiceDate")
    private String taxInvoiceDate;

    // Parent SanctionOrderResponse fields needed by JRXML Dataset1
    private Integer serialNumber;
    private String vendorName;
    private String machineTypeName;
    private String rearingEquipmentDetailsNameInKannada;
    private Float centralSanctionAmount;
    private Float stateSanctionAmount;
    private Float unitCost;
    private Double beneficiaryAmount;
}

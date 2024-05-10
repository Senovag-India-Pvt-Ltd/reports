package com.sericulture.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class SupplyOrderPrintRequest extends  LotStatusRequest {
    private int applicationFormId;

}

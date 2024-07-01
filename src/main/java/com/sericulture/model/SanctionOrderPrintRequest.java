package com.sericulture.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class SanctionOrderPrintRequest extends LotStatusRequest {
    private int applicationFormId;
}

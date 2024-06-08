package com.sericulture.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class SanctionBeneficiaryPrintRequest extends LotStatusRequest{
    private int applicationFormId;
}

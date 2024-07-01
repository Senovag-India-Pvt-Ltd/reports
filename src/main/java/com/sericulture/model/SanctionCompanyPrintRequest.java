package com.sericulture.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class SanctionCompanyPrintRequest extends LotStatusRequest {
    private int applicationFormId;
}

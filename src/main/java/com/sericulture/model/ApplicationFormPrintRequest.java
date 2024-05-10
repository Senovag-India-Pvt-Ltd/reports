package com.sericulture.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ApplicationFormPrintRequest extends LotStatusRequest {

    private int applicationFormId;
}

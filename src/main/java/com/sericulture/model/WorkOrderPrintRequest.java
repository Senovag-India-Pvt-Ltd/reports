package com.sericulture.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class WorkOrderPrintRequest extends LotStatusRequest {
    private int applicationFormId;
    private int scApplicationFormServiceId;
    private int stepId;
    private int schemeId;
    private int userId;

}

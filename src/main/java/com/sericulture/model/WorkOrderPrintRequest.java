package com.sericulture.model;

import lombok.*;

import java.util.List;

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


    private int userMasterId;
    private List<Long> applicationFormIds;

    private int subSchemeId;
    private int categoryId;

}

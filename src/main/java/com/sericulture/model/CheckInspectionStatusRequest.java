package com.sericulture.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CheckInspectionStatusRequest extends LotStatusRequest {
    private int applicationFormId;
    private int scApplicationFormServiceId;
    private int stepId;
    private int schemeId;
    private int userMasterId;
    private int subSchemeId;
    private int componentId;
    private String sanctionOrderNumber;

    private List<Long> applicationFormIds;
}

package com.sericulture.model;

import lombok.*;

import java.time.LocalDate;
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

    private int categoryId;

    private List<Long> applicationFormIds;

    private Float unitPrice;
    private String reelingShedDetails;
    private String reelingShedSqft;
    private LocalDate auctionDate;
}

package com.sericulture.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ReelerPendingReportResponse extends ResponseBody {
    private String balance;
    private String creditTotal;
    private String debitTotal;
    private String marketName;
    List<ReelerPendingInfo> reelerPendingInfoList;

    private String headerText;
}
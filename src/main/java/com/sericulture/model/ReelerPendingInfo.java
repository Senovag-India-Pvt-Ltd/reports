package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReelerPendingInfo extends ResponseBody {
    private String reelerName;
    private String reelerNumber;
    private String reelingLicenseNumber;
    private String mobileNumber;
    private String lastTxnTime;
    private String currentBalance;
    private String serialNumber;
    private String counter;
    private String onlineTxn;
    private String suspend;

    private String headerText;
    private String credit;
    private String debit;
    private String deposit;
}
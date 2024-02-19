package com.sericulture.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class LotReportResponse extends ResponseBody {
    private int lotId;
    private String reelerLicenseNumber;
    private int bidAmount;
    private String bidTime;
    private String accepted;
    private int auctionNumber;
    private String acceptedTime;
    private String acceptedBy;

    private String headerText;
    private String headerText2;

    private String reelerNumber;
}
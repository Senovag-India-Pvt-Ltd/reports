package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DashboardReportInfo {
    private String raceName;
    private String totalLots;
    private String totalLotsBid;
    private String totalLotsNotBid;
    private String totalBids;
    private String totalReelers;
    private String currentAuctionMaxBid;
    private String accecptedLots;
    private String accecptedLotsMaxBid;
    private String accectedLotsMinBid;
    private String averagRate;
    private String weighedLots;
    private String totalSoldOutAmount;

    private String totalBidCount;
    private String currentAuctionMaxAmount;
    private String totalNotBid;

    private String headerText;
}

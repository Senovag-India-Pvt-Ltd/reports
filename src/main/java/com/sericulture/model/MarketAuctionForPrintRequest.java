package com.sericulture.model;


import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class MarketAuctionForPrintRequest extends LotStatusRequest{

    private LocalDate auctionDate;
}

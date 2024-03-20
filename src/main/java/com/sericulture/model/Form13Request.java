package com.sericulture.model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Form13Request extends RequestBody{
    private LocalDate auctionDate;
}
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
    private String marketName;
    private String currentBalance;
    private String totalAmount;

    private String grandTotalAmount;
    private String headerText;
}
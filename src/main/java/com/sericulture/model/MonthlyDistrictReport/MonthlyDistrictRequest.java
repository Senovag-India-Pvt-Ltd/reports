package com.sericulture.model.MonthlyDistrictReport;

import com.sericulture.model.RequestBody;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class MonthlyDistrictRequest extends RequestBody {

    private LocalDate startDate;
    private LocalDate endDate;
    private int marketId;
}
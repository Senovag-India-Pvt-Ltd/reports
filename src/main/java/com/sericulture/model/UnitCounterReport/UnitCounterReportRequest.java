package com.sericulture.model.UnitCounterReport;

import com.sericulture.model.RequestBody;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UnitCounterReportRequest extends RequestBody {
    private LocalDate reportFromDate;
    private int godownId;
    private int marketId;
}

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
    private LocalDate fromDate;
    private LocalDate toDate;
    private int godownId;
    private int marketId;
    private String reelerNumber;
    private String traderLicenseNumber;

}

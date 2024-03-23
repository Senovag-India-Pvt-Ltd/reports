package com.sericulture.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AverageReportRequest extends RequestBody {

    private int startYear;
    private int endYear;
}
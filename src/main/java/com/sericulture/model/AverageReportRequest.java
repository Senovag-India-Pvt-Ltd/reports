package com.sericulture.model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AverageReportRequest extends RequestBody {

    private LocalDate startYear;
    private LocalDate endYear;
}
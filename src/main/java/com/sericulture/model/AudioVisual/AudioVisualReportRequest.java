package com.sericulture.model.AudioVisual;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.RequestBody;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioVisualReportRequest extends RequestBody {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> marketList;
}
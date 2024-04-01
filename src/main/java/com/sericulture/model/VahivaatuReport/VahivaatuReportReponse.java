package com.sericulture.model.VahivaatuReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class VahivaatuReportReponse {
    private VahivaatuReport vahivaatuReport;
}
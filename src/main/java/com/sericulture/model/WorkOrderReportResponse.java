package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "ResponseWrapper")
public class WorkOrderReportResponse {
    private List<WorkOrderGenerationReportResponse> content;
    private int errorCode;

}

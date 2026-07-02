package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ARMSanctionOrder {
    private List<ARMSanctionResponse> content;
    private int errorCode;
    private List<String> errorMessages;
}

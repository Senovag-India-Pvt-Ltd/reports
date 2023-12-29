package com.sericulture.model;


import com.sericulture.model.exceptions.ValidationMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestBody implements Serializable {

    @Schema(name = "marketId", example = "1", required = true)
    private int marketId;

    @Schema(name = "godownId", example = "1", required = false)
    private int godownId;

    public List<ValidationMessage> validate(){
        return new ArrayList<ValidationMessage>();
    }
}

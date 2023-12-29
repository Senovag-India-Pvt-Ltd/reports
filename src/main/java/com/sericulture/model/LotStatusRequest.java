package com.sericulture.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LotStatusRequest extends RequestBody {

    @Schema(name = "allottedLotId", example = "1", required = true)
    private int allottedLotId;
}

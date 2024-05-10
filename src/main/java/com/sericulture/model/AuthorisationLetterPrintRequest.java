package com.sericulture.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AuthorisationLetterPrintRequest extends LotStatusRequest{
    private int applicationFormId;

}

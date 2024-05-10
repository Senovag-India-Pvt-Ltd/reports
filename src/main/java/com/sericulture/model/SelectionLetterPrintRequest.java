package com.sericulture.model;

import lombok.*;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class SelectionLetterPrintRequest extends LotStatusRequest{

        private int applicationFormId;
}

package com.sericulture.model;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ReelerTransactionReport {
    String operationDescription;

    String transactionDate;

    String depositAmount;

    String paymentAmount;

    String balance;

    String transactionType;

    String reeler_amount_balance;
    String farmer_details_farmer_transaction;
    String total_sale_amount_farmer_transaction;
    String headerText;
}
package com.setu.biller.dto;

import lombok.Data;

/**
 * @author Krishna Verma
 * @date 21/07/2020
 */
@Data
public class BillPaymentUpdateResponse {
    private String status;

    private PaymentAcknowledgement data;
}


package com.setu.biller.service;

import com.setu.biller.dto.BillPaymentRequest;
import com.setu.biller.dto.BillPaymentUpdateResponse;
import com.setu.biller.dto.DueResponse;
import com.setu.biller.exception.BillerException;

/**
 * @author Krishna Verma
 * @date 21/07/2020
 */
public interface BillPaymentService {

    DueResponse getBillOfPaymentDue(String mobileNumber) throws BillerException;

    BillPaymentUpdateResponse updatePayment(BillPaymentRequest billPaymentRequest) throws BillerException;
}

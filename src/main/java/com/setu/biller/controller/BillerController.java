package com.setu.biller.controller;

import com.setu.biller.dto.BillPaymentRequest;
import com.setu.biller.dto.BillPaymentUpdateResponse;
import com.setu.biller.dto.DueResponse;
import com.setu.biller.dto.ErrorResponse;
import com.setu.biller.exception.BillerException;
import com.setu.biller.service.BillPaymentServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.setu.biller.utils.Constants.*;

/**
 * @author Krishna Verma
 * @date 21/07/2020
 */
@RestController
@RequestMapping(BASE)
@Slf4j
public class BillerController {

    private final BillPaymentServiceImpl billPaymentService;

    @Autowired
    public BillerController(BillPaymentServiceImpl billPaymentService) {
        this.billPaymentService = billPaymentService;
    }

    @PostMapping(FETCH_BILL)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 400, message = INVALID_API_PARAMETERS),
            @ApiResponse(code = 403, message = AUTH_ERROR),
            @ApiResponse(code = 404, message = CUSTOMER_NOT_FOUND),
            @ApiResponse(code = 500, message = UNHANDLED_ERROR)
    })
    @ApiOperation(
            value = "Fetches the Payment Due Bill",
            notes = "Fetches the Payment Due Bill")
    public ResponseEntity<?> getBillPaymentDue(@RequestBody Map<String, String> requestPayload) {
        final String mobileNumber = requestPayload.get(MOBILE_NUMBER);
        log.info("Inside the fetch api to return the bill payment due for mobileNumber: " + mobileNumber);
        DueResponse billOfPaymentDue = null;
        try {
            billOfPaymentDue = billPaymentService.getBillOfPaymentDue(mobileNumber);
        } catch (BillerException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(ERROR);
            errorResponse.setErrorCode(e.getMessage());
            if (e.getMessage().equals(INVALID_API_PARAMETERS)) {
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            } else if (e.getMessage().equals(CUSTOMER_NOT_FOUND)) {
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        }
        assert billOfPaymentDue != null;
        return new ResponseEntity<>(billOfPaymentDue, HttpStatus.OK);
    }

    @PostMapping(PAYMENT_UPDATE)
    @ApiOperation(value = "Update the Due Bill with Payment", notes = "Update the Due Bill with Payment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 400, message = INVALID_API_PARAMETERS),
            @ApiResponse(code = 403, message = AUTH_ERROR),
            @ApiResponse(code = 404, message = CUSTOMER_NOT_FOUND),
            @ApiResponse(code = 500, message = UNHANDLED_ERROR)
    })
    public ResponseEntity<?> paymentUpdate(@RequestBody BillPaymentRequest billPaymentRequest) {
        log.info("Inside the payment update api to update the payment for refID: " + billPaymentRequest.getRefID());
        BillPaymentUpdateResponse billPaymentUpdateResponse = null;
        try {
            billPaymentUpdateResponse = billPaymentService.updatePayment(billPaymentRequest);
        } catch (BillerException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(ERROR);
            errorResponse.setErrorCode(e.getMessage());
            if (e.getMessage().equals(INVALID_API_PARAMETERS) || e.getMessage().equals(AMOUNT_MISMATCH)) {
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            } else if (e.getMessage().equals(CUSTOMER_NOT_FOUND) || e.getMessage().equals(INVALID_REFERENCE_ID)) {
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(billPaymentUpdateResponse, HttpStatus.OK);
    }


}

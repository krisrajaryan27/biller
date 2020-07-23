package com.setu.biller.service;

import com.setu.biller.dto.*;
import com.setu.biller.entities.Customer;
import com.setu.biller.exception.BillerException;
import com.setu.biller.repository.BillPaymentRepository;
import com.setu.biller.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Krishna Verma
 * @date 22/07/2020
 */
@Service
@Slf4j
public class BillPaymentServiceImpl implements BillPaymentService {


    private final BillPaymentRepository billPaymentRepository;

    @Autowired
    public BillPaymentServiceImpl(BillPaymentRepository billPaymentRepository) {
        this.billPaymentRepository = billPaymentRepository;
    }

    @Override
    public DueResponse getBillOfPaymentDue(String mobileNumber) throws BillerException {
        log.debug("Inside PaymentDueBill Method...");
        if (mobileNumber == null || mobileNumber.length() != 10) {
            log.error("Mobile Number is invalid!");
            throw new BillerException(Constants.INVALID_API_PARAMETERS);
        }
        Optional<Customer> customerByMobileNumber = billPaymentRepository.findByMobileNumber(mobileNumber);
        if (customerByMobileNumber.isPresent()) {
            Customer customer = customerByMobileNumber.get();
            DueResponse dueResponse = new DueResponse();
            dueResponse.setStatus(Constants.SUCCESS);
            DueDataResponse dueDataResponse = new DueDataResponse();
            dueDataResponse.setCustomerName(customer.getCustomerName());
            dueDataResponse.setDueAmount(customer.getDueAmount());
            dueDataResponse.setDueDate(customer.getDueDate());
            dueDataResponse.setRefID(customer.getRefID());
            dueResponse.setData(dueDataResponse);
            return dueResponse;
        } else {
            log.error("Customer not found for mobile number: {}", mobileNumber);
            throw new BillerException(Constants.CUSTOMER_NOT_FOUND);
        }
    }

    @Override
    public BillPaymentUpdateResponse updatePayment(BillPaymentRequest billPaymentRequest) throws BillerException {
        log.debug("Inside updatePayment method...");
        if (billPaymentRequest == null) {
            log.error("BillPaymentRequest is null!");
            throw new BillerException(Constants.INVALID_API_PARAMETERS);
        }
        Transaction transaction = billPaymentRequest.getTransaction();
        if (transaction == null || transaction.getAmountPaid() == null || transaction.getDate() == null || transaction.getId() == null) {
            log.error("BillPaymentRequest Transaction parameter is invalid!");
            throw new BillerException(Constants.INVALID_API_PARAMETERS);
        }
        if (billPaymentRequest.getRefID() == null || billPaymentRequest.getRefID().isEmpty()) {
            log.error("RefID in the BillPayment Request is either null or empty!");
            throw new BillerException(Constants.INVALID_REFERENCE_ID);
        }
        Optional<Customer> customerByRefID = billPaymentRepository.findByRefID(billPaymentRequest.getRefID());
        if (customerByRefID.isPresent()) {
            Customer customer = customerByRefID.get();
            BillPaymentUpdateResponse billPaymentUpdateResponse = new BillPaymentUpdateResponse();
            PaymentAcknowledgement paymentAcknowledgement = new PaymentAcknowledgement();
            if (customer.isPaid()) {
                billPaymentUpdateResponse.setStatus(Constants.SUCCESS);
                paymentAcknowledgement.setAckID(customer.getAckID());
                billPaymentUpdateResponse.setData(paymentAcknowledgement);
                return billPaymentUpdateResponse;
            }
            customer.setAckID(String.valueOf(UUID.randomUUID()));
            if (!transaction.getAmountPaid().equals(customer.getDueAmount())) {
                log.error("Amount sent for payment of bill does not matches with due amount!");
                throw new BillerException(Constants.AMOUNT_MISMATCH);
            }
            customer.setAmountPaid(transaction.getAmountPaid());
            customer.setDate(transaction.getDate());
            customer.setDueAmount("0");
            customer.setPaid(true);
            billPaymentRepository.save(customer);
            billPaymentUpdateResponse.setStatus(Constants.SUCCESS);
            paymentAcknowledgement.setAckID(customer.getAckID());
            billPaymentUpdateResponse.setData(paymentAcknowledgement);
            return billPaymentUpdateResponse;
        } else {
            log.error("Customer is not present for the given refID: {}", billPaymentRequest.getRefID());
            throw new BillerException(Constants.INVALID_REFERENCE_ID);
        }
    }
}

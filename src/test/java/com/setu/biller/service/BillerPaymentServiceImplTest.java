package com.setu.biller.service;

import com.setu.biller.dto.BillPaymentRequest;
import com.setu.biller.dto.BillPaymentUpdateResponse;
import com.setu.biller.dto.DueResponse;
import com.setu.biller.dto.Transaction;
import com.setu.biller.entities.Customer;
import com.setu.biller.exception.BillerException;
import com.setu.biller.repository.BillPaymentRepository;
import com.setu.biller.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.setu.biller.utils.Constants.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BillerPaymentServiceImplTest {

    private final BillPaymentRepository billPaymentRepository = mock(BillPaymentRepository.class);
    private BillPaymentService billPaymentService;
    private Customer customer;

    @BeforeEach
    void initUseCase() {
        billPaymentService = new BillPaymentServiceImpl(billPaymentRepository);
        customer = CustomerInitialisation.populateInitialCustomer();
    }

    @Test
    void testGetBillOfPaymentDue() throws BillerException {
        when(billPaymentRepository.findByMobileNumber(customer.getMobileNumber())).thenReturn(Optional.of(customer));
        DueResponse billOfPaymentDue = billPaymentService.getBillOfPaymentDue(customer.getMobileNumber());
        assert billOfPaymentDue != null;
        assertEquals(SUCCESS, billOfPaymentDue.getStatus());
    }

    @Test
    void testGetBillOfPaymentDueWhenMobileNumberIsInvalid() {
        Assertions.assertThrows(BillerException.class, () -> billPaymentService.getBillOfPaymentDue("123456789"), Constants.INVALID_REFERENCE_ID);
    }

    @Test
    void testGetBillOfPaymentDueWhenCustomerIsNotPresentForMobileNumber() {
        when(billPaymentRepository.findByMobileNumber(customer.getMobileNumber())).thenReturn(Optional.empty());
        Assertions.assertThrows(BillerException.class, () -> billPaymentService.getBillOfPaymentDue(customer.getMobileNumber()), Constants.INVALID_REFERENCE_ID);
    }


    @Test
    void testUpdatePayment() throws BillerException {
        when(billPaymentRepository.findByRefID(customer.getRefID())).thenReturn(Optional.of(customer));
        BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
        billPaymentRequest.setRefID(customer.getRefID());
        Transaction transaction = new Transaction();
        transaction.setAmountPaid(customer.getDueAmount());
        transaction.setDate(customer.getDueDate());
        transaction.setId(UUID.randomUUID().toString());
        billPaymentRequest.setTransaction(transaction);
        BillPaymentUpdateResponse billPaymentUpdateResponse = billPaymentService.updatePayment(billPaymentRequest);
        assert billPaymentUpdateResponse != null;
        assertEquals(SUCCESS, billPaymentUpdateResponse.getStatus());
    }

    @Test
    void testUpdatePaymentWhenAlreadyPaid() throws BillerException {
        customer.setPaid(true);
        when(billPaymentRepository.findByRefID(customer.getRefID())).thenReturn(Optional.of(customer));
        BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
        billPaymentRequest.setRefID(customer.getRefID());
        Transaction transaction = new Transaction();
        transaction.setAmountPaid(customer.getDueAmount());
        transaction.setDate(customer.getDueDate());
        transaction.setId(UUID.randomUUID().toString());
        billPaymentRequest.setTransaction(transaction);
        BillPaymentUpdateResponse billPaymentUpdateResponse = billPaymentService.updatePayment(billPaymentRequest);
        assert billPaymentUpdateResponse != null;
        assertEquals(SUCCESS, billPaymentUpdateResponse.getStatus());
        customer.setPaid(false);
    }

    @Test
    void testUpdatePaymentWhenBillPaymentRequestIsNull() {
        Assertions.assertThrows(BillerException.class, () -> billPaymentService.updatePayment(null), Constants.INVALID_API_PARAMETERS);
    }

    @Test
    void testUpdatePaymentWhenTransactionIsNull() {
        BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
        billPaymentRequest.setRefID(customer.getRefID());
        Assertions.assertThrows(BillerException.class, () -> billPaymentService.updatePayment(billPaymentRequest), Constants.INVALID_API_PARAMETERS);
    }

    @Test
    void testUpdatePaymentWhenRefIDIsNull() {
        BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
        billPaymentRequest.setRefID(null);
        Transaction transaction = new Transaction();
        transaction.setAmountPaid(customer.getDueAmount());
        transaction.setDate(customer.getDueDate());
        transaction.setId(UUID.randomUUID().toString());
        billPaymentRequest.setTransaction(transaction);
        Assertions.assertThrows(BillerException.class, () -> billPaymentService.updatePayment(billPaymentRequest), Constants.INVALID_REFERENCE_ID);
    }

    @Test
    void testUpdatePaymentWhenDueAmountDoesNotMatchesWithAmountPaid() {
        when(billPaymentRepository.findByRefID(customer.getRefID())).thenReturn(Optional.of(customer));
        BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
        billPaymentRequest.setRefID(customer.getRefID());
        Transaction transaction = new Transaction();
        transaction.setAmountPaid("1000");
        transaction.setDate(customer.getDueDate());
        transaction.setId(UUID.randomUUID().toString());
        billPaymentRequest.setTransaction(transaction);
        Assertions.assertThrows(BillerException.class, () -> billPaymentService.updatePayment(billPaymentRequest), Constants.AMOUNT_MISMATCH);
    }

    @Test
    void testUpdatePaymentWhenCustomerIsNotPresentForRefID() {
        when(billPaymentRepository.findByRefID(customer.getRefID())).thenReturn(Optional.empty());
        BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
        billPaymentRequest.setRefID(customer.getRefID());
        Transaction transaction = new Transaction();
        transaction.setAmountPaid("1000");
        transaction.setDate(customer.getDueDate());
        transaction.setId(UUID.randomUUID().toString());
        billPaymentRequest.setTransaction(transaction);
        Assertions.assertThrows(BillerException.class, () -> billPaymentService.updatePayment(billPaymentRequest), Constants.INVALID_REFERENCE_ID);
    }

}

package com.setu.biller.repository;

import com.setu.biller.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Krishna Verma
 * @date 22/07/2020
 */
@Repository
public interface BillPaymentRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByMobileNumber(String mobileNumber);

    Optional<Customer> findByRefID(String refID);
}


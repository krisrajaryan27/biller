package com.setu.biller.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Krishna Verma
 * @date 22/07/2020
 */
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "mobile_number", unique = true, nullable = false)
    private String mobileNumber;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "due_amount")
    private String dueAmount;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "ref_id")
    private String refID;

    @Column(name = "amount_paid")
    private String amountPaid;

    @Column(name = "ack_id")
    private String ackID;

    @Column(name = "date")
    private String date;

}

package com.setu.biller.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Krishna Verma
 * @date 21/07/2020
 */
@Data
public class DueDataResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String customerName;

    private String dueAmount;

    private String dueDate;

    private String refID;

}

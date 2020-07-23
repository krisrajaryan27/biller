package com.setu.biller.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Krishna Verma
 * @date 23/07/2020
 */
@Data
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private String errorCode;
}

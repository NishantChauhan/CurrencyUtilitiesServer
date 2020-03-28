package com.nishant.springboot.currencyutilities.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class Rates {
    private Boolean success;
    private Long timestamp;
    private String base;
    private Date date;
    private Map<String, Double> rates;
    private APIError error;

}

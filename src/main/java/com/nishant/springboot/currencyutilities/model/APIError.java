package com.nishant.springboot.currencyutilities.model;

import lombok.Data;

@Data
public class APIError {
    Integer code;
    String type;
    String info;
}

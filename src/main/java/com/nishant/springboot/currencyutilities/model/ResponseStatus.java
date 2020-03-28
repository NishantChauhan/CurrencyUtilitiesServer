package com.nishant.springboot.currencyutilities.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseStatus {
    @NotNull
    String status;
    String errorCode, errorDescription;
}

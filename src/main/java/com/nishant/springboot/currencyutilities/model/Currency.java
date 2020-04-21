package com.nishant.springboot.currencyutilities.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Currency implements Comparable {
    String currencyName;
    String currencySymbol;

    @Override
    public int compareTo(Object o) {
        if(o instanceof Currency){
            Currency currency = (Currency) o;
            return currencySymbol.compareTo(((Currency) o).currencySymbol);
        }
        return 0;
    }
}

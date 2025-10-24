package com.example.factory.factory;

import com.example.factory.exception.UnsupportedCountryException;
import com.example.factory.interfaces.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SaleFactory {
    private final Map<String, Sale> salesByCountry;

    @Autowired
    public SaleFactory(Map<String, Sale> salesByCountry) {
        this.salesByCountry = salesByCountry;
    }

    public Sale getSaleByCountry(String countryCode) {
        Sale sale = salesByCountry.get(countryCode.toUpperCase());
        if (sale == null) {
            throw new UnsupportedCountryException("País no soportado: " + countryCode);
        }
        return sale;
    }
}

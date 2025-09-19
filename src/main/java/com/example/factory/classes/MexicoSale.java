package com.example.factory.classes;

import com.example.factory.interfaces.Sale;
import org.springframework.stereotype.Component;

@Component("MX")
public class MexicoSale implements Sale {
    private final double vatRate = 0.16;

    @Override
    public double calculatePriceWithVAT(double baseAmount){
        return baseAmount + ( baseAmount * vatRate);
    }

    @Override
    public double getVatRate() {
        return vatRate;
    }
}

package com.example.factory.classes;

import com.example.factory.interfaces.Sale;
import org.springframework.stereotype.Component;

@Component("CL")
public class ChileSale implements Sale {
    private final double vatRate = 0.19;

    @Override
    public double calculatePriceWithVAT(double baseAmount){
        return baseAmount + ( baseAmount * vatRate);
    }

    @Override
    public double getVatRate() {
        return vatRate;
    }
}

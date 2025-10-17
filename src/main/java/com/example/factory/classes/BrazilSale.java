package com.example.factory.classes;

import com.example.factory.interfaces.Sale;
import org.springframework.stereotype.Component;

@Component("BR")
public class BrazilSale implements Sale {

    private final double vatRate = 0.12;

    @Override
    public double calculatePriceWithVAT(double baseAmount){
        return baseAmount + ( baseAmount * vatRate);
    }

    @Override
    public double getVatRate() {
        return vatRate;
    }
}

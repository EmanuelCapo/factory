package com.example.factory.interfaces;

public interface Sale {

    default double calculatePriceWithVAT(double baseAmount){
        return baseAmount;
    }

    double getVatRate();
}

package com.example.factory.classes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SaleResponse {
    private String country;
    private double baseAmount;
    private double vatRate;
    private double finalAmount;
}

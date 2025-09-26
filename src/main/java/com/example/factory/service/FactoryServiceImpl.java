package com.example.factory.service;

import com.example.factory.classes.dto.SaleRequest;
import com.example.factory.classes.dto.SaleResponse;
import com.example.factory.factory.SaleFactory;
import com.example.factory.interfaces.Sale;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FactoryServiceImpl implements FactoryService{
    private final SaleFactory saleFactory;

    @Override
    public SaleResponse calculatePrice(SaleRequest request) {
        Sale sale = saleFactory.getSaleByCountry(request.getCountry());
        double finalAmount = sale.calculatePriceWithVAT(request.getAmount());
        double vatRate = sale.getVatRate();

        SaleResponse response = new SaleResponse(
                request.getCountry().toUpperCase(),
                request.getAmount(),
                vatRate,
                finalAmount
        );
        return response;
    }
}

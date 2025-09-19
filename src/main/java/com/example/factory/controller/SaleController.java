package com.example.factory.controller;

import com.example.factory.classes.dto.SaleRequest;
import com.example.factory.classes.dto.SaleResponse;
import com.example.factory.factory.SaleFactory;
import com.example.factory.interfaces.Sale;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
@AllArgsConstructor
public class SaleController {
    private final SaleFactory saleFactory;

    @PostMapping("/price")
    public ResponseEntity<?> calculatePrice(@RequestBody SaleRequest request) {
        try {
            Sale sale = saleFactory.getSaleByCountry(request.getCountry());
            double finalAmount = sale.calculatePriceWithVAT(request.getAmount());
            double vatRate = sale.getVatRate();

            SaleResponse response = new SaleResponse(
                    request.getCountry().toUpperCase(),
                    request.getAmount(),
                    vatRate,
                    finalAmount
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

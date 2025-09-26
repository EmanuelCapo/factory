package com.example.factory.controller;

import com.example.factory.classes.dto.SaleRequest;
import com.example.factory.classes.dto.SaleResponse;
import com.example.factory.service.FactoryService;
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
    private final FactoryService factoryService;

    @PostMapping("/price")
    public ResponseEntity<?> calculatePrice(@RequestBody SaleRequest request) {
        try {
            SaleResponse response = factoryService.calculatePrice(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

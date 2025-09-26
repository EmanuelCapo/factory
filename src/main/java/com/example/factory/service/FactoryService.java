package com.example.factory.service;

import com.example.factory.classes.dto.SaleRequest;
import com.example.factory.classes.dto.SaleResponse;

public interface FactoryService {
    SaleResponse calculatePrice(SaleRequest request);
}

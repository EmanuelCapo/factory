package com.example.factory.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface SaleExcelService {
    byte[] generateTemplate();
    Map<String, Object> processExcel(MultipartFile file);
}

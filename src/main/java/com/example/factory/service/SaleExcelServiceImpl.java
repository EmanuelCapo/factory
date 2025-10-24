package com.example.factory.service;

import com.example.factory.classes.dto.SaleRequest;
import com.example.factory.classes.dto.SaleResponse;
import com.example.factory.exception.InvalidExcelFormatException;
import com.example.factory.exception.UnsupportedCountryException;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SaleExcelServiceImpl implements SaleExcelService{
    private final FactoryService factoryService;

    @Override
    public byte[] generateTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet dataSheet = workbook.createSheet("SalesData");
            Row header = dataSheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            headerStyle.setFont(boldFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] headers = {"country", "amount"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            Sheet versionSheet = workbook.createSheet("Version");
            Row row1 = versionSheet.createRow(0);
            row1.createCell(0).setCellValue("version");
            row1.createCell(1).setCellValue("date");

            Row row2 = versionSheet.createRow(1);
            row2.createCell(0).setCellValue("1.0");
            row2.createCell(1).setCellValue("2025-10-18");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel template", e);
        }
    }

    @Override
    public Map<String, Object> processExcel(MultipartFile file) {
        List<Map<String, Object>> results = new ArrayList<>();
        List<Map<String, Object>> errors = new ArrayList<>();

        int processed = 0;
        int success = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheet("SalesData");
            if (sheet == null) {
                throw new InvalidExcelFormatException("Hoja 'SalesData' no encontrada");
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null || headerRow.getPhysicalNumberOfCells() < 2) {
                throw new InvalidExcelFormatException("Cabecera inválida");
            }

            if (!"country".equalsIgnoreCase(headerRow.getCell(0).getStringCellValue()) ||
                    !"amount".equalsIgnoreCase(headerRow.getCell(1).getStringCellValue())) {
                throw new InvalidExcelFormatException("Columnas esperadas: country, amount");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                processed++;

                String country = row.getCell(0).getStringCellValue().trim();
                double amount = row.getCell(1).getNumericCellValue();

                try {
                    SaleResponse response = factoryService.calculatePrice(SaleRequest.builder().country(country).amount(amount).build());
                    results.add(Map.of(
                            "country", response.getCountry(),
                            "baseAmount", response.getBaseAmount(),
                            "finalAmount", response.getFinalAmount()
                    ));
                    success++;
                } catch (UnsupportedCountryException e) {
                    errors.add(Map.of(
                            "row", i + 1,
                            "country", country,
                            "error", e.getMessage()
                    ));
                }
            }

        } catch (IOException e) {
            throw new InvalidExcelFormatException("Error al leer el archivo Excel");
        }

        return Map.of(
                "processed", processed,
                "success", success,
                "errors", errors,
                "results", results
        );
    }
}

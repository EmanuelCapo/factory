package com.example.factory;

import com.example.factory.classes.ChileSale;
import com.example.factory.classes.MexicoSale;
import com.example.factory.classes.dto.SaleRequest;
import com.example.factory.classes.dto.SaleResponse;
import com.example.factory.classes.BrazilSale;
import com.example.factory.factory.SaleFactory;
import com.example.factory.interfaces.Sale;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SaleFactoryTest {
    @Autowired
    private SaleFactory saleFactory;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaleFactory() throws Exception {
        // Brasil
        Sale saleBR = saleFactory.getSaleByCountry("BR");

        assertNotNull(saleBR);
        assertInstanceOf(BrazilSale.class, saleBR);
        double baseAmount = 100.0;
        double resultBR = saleBR.calculatePriceWithVAT(baseAmount);
        double expectedResultBR = 112.0;

        assertEquals(expectedResultBR, resultBR, 0.001);

        //Chile
        Sale saleCL = saleFactory.getSaleByCountry("CL");

        assertNotNull(saleCL);
        assertInstanceOf(ChileSale.class, saleCL);
        double resultCL = saleCL.calculatePriceWithVAT(baseAmount);
        double expectedResultCL = 119.0;

        assertEquals(expectedResultCL, resultCL, 0.001);

        //Mexico
        Sale saleMX = saleFactory.getSaleByCountry("MX");

        assertNotNull(saleMX);
        assertInstanceOf(MexicoSale.class, saleMX);
        double resultMX = saleMX.calculatePriceWithVAT(baseAmount);
        double expectedResultMX = 116.0;

        assertEquals(expectedResultMX, resultMX, 0.001);

        // No soportado
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                saleFactory.getSaleByCountry("AR")
        );
        assertEquals("País no soportado: AR", exception.getMessage());
    }

    @Test
    void testDefaultMethod() {
        Sale sale = new Sale() {
            @Override
            public double getVatRate() { return 0; }
        };

        double baseAmount = 100.0;
        double result = sale.calculatePriceWithVAT(baseAmount);

        assertEquals(baseAmount, result, 0.001);
    }

    @Test
    void testController() throws Exception {
        // Brasil
        SaleRequest requestBR = new SaleRequest("BR", 100.0);
        SaleResponse expectedResponseBR = new SaleResponse("BR", 100.0, 0.12, 112.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBR)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBR)));

        // Chile
        SaleRequest requestCL = new SaleRequest("CL", 100.0);
        SaleResponse expectedResponseCL = new SaleResponse("CL", 100.0, 0.19, 119.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales/price")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(requestCL)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseCL)));

        // Mexico
        SaleRequest requestMX = new SaleRequest("MX", 100.0);
        SaleResponse expectedResponseMX = new SaleResponse("MX", 100.0, 0.16, 116.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMX)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseMX)));

        // País no soportado
        SaleRequest requestAR = new SaleRequest("AR", 100.0);
        String expectedResponseAR = "País no soportado: " + requestAR.getCountry();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestAR)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedResponseAR));
    }
}

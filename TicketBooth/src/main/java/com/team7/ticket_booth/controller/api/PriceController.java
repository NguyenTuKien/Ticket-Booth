package com.team7.ticket_booth.controller.api;

import com.team7.ticket_booth.model.Price;
import com.team7.ticket_booth.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prices")
public class PriceController {
    private final PriceService priceService;

    @GetMapping("")
    public List<Price> getPrices() {
        return priceService.getAllPrices();
    }
}

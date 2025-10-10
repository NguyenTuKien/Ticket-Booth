package com.team7.ticket_booth.controller.admin;

import com.team7.ticket_booth.dto.request.PriceRequestDTO;
import com.team7.ticket_booth.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/prices")
@RequiredArgsConstructor
public class AdminPriceController {
    private final PriceService priceService;

    @PutMapping("/update")
    public void updatePrice(@RequestBody PriceRequestDTO dto) {
        priceService.updatePrice(dto);
    }

    @PutMapping("/increase")
    public void increasePrice(@RequestBody PriceRequestDTO dto) {
        priceService.incresePrice(dto);
    }

}

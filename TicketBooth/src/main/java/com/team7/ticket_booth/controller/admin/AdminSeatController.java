package com.team7.ticket_booth.controller.admin;

import com.team7.ticket_booth.dto.request.SeatRequestDTO;
import com.team7.ticket_booth.dto.response.SeatResponseDTO;
import com.team7.ticket_booth.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/seats")
@RequiredArgsConstructor
public class AdminSeatController {
    private final SeatService seatService;

    @GetMapping("/{name}")
    public List<SeatResponseDTO> getAllSeatsOfHall(@RequestParam String name) {
        return seatService.getSeatsByHallName(name);
    }

    @PutMapping("")
    public void updateSeatStatus(@RequestParam UUID seatId, @RequestBody SeatRequestDTO seatRequestDTO) {
        seatService.updateSeat(seatId, seatRequestDTO);
    }

    @DeleteMapping("")
    public void deleteSeat(@RequestParam UUID seatId) {
        seatService.deleteSeat(seatId);
    }
}

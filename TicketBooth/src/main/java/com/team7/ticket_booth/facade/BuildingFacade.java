package com.team7.ticket_booth.facade;

import com.team7.ticket_booth.dto.request.HallRequestDTO;
import com.team7.ticket_booth.dto.response.HallResponseDTO;
import com.team7.ticket_booth.model.Hall;
import com.team7.ticket_booth.repository.SeatRepository;
import com.team7.ticket_booth.service.HallService;
import com.team7.ticket_booth.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuildingFacade {
    private final HallService hallService;
    private final SeatService seatService;

    public void buildHall(HallRequestDTO hallRequestDTO) {
        HallResponseDTO hall = hallService.createHall(hallRequestDTO);
        seatService.createSeat(hall);
    }

    public void collaspeHall(HallRequestDTO hallRequestDTO) {
        hallService.deleteHall(hallRequestDTO);
    }

    public void repairHall(HallRequestDTO hallRequestDTO) {
        HallResponseDTO hall = hallService.updateHall(hallRequestDTO);
        seatService.updateAllSeatInHall(hall);
    }
}

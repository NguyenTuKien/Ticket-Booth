package com.team7.ticket_booth.controller.admin;

import com.team7.ticket_booth.dto.request.HallRequestDTO;
import com.team7.ticket_booth.facade.BuildingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/halls")
@RequiredArgsConstructor
public class AdminHallController {
    private final BuildingFacade buildingFacade;

    @PostMapping("")
    public void buildHall(@RequestBody HallRequestDTO hallRequestDTO) {
        buildingFacade.buildHall(hallRequestDTO);
    }

    @DeleteMapping("")
    public void collaspeHall(@RequestBody HallRequestDTO hallRequestDTO) {
        buildingFacade.collaspeHall(hallRequestDTO);
    }

    @PutMapping("")
    public void repairHall(@RequestBody HallRequestDTO hallRequestDTO) {
        buildingFacade.repairHall(hallRequestDTO);
    }
}

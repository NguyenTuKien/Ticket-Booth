package com.team7.ticket_booth.controller.admin;

import com.team7.ticket_booth.dto.request.ShowRequestDTO;
import com.team7.ticket_booth.dto.response.ShowResponseDTO;
import com.team7.ticket_booth.model.Show;
import com.team7.ticket_booth.service.ShowService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/admin/shows")
@RequiredArgsConstructor
public class AdminShowController {
    private final ShowService showService;

    @PutMapping("")
    public void updateShow(@RequestParam UUID id, @RequestBody ShowRequestDTO dto){
        Show show = showService.getShowById(id);
    }

}

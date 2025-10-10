package com.team7.ticket_booth.controller.api;

import com.team7.ticket_booth.dto.response.ShowResponseDTO;
import com.team7.ticket_booth.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
public class ShowController {
    final private ShowService showService;

    @GetMapping("/all")
    public List<ShowResponseDTO> getAllShows(@RequestParam int page) {
        return showService.getAllShowsOrderByTime(page);
    }
}

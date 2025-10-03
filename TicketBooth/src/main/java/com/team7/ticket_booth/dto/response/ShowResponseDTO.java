package com.team7.ticket_booth.dto.response;

import com.team7.ticket_booth.model.Show;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ShowResponseDTO {
    UUID id;
    String movieTitle;
    String hallName;
    LocalTime startTime;
    LocalTime endTime;
    LocalDate showDate;

    public ShowResponseDTO(Show show) {
        this.id = show.getId();
        this.movieTitle = show.getMovie().getTitle();
        this.hallName = show.getHall().getName();
        this.startTime = show.getShift().getStartTime();
        this.endTime = show.getShift().getEndTime();
        this.showDate = show.getShowDate();
    }
}

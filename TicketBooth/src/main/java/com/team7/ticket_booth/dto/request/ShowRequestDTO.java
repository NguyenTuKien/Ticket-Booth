package com.team7.ticket_booth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ShowRequestDTO {
    @JsonProperty("MovieTitle")
    private String movieTitle;
    @JsonProperty("StartTimes")
    @JsonFormat(pattern = "HH:mm")
    List<LocalTime> startTimes;
}

package com.team7.ticket_booth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class MovieRequestDTO {
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("ThumbnailUrl")
    private String thumbnailUrl;
    @JsonProperty("Duration")
    private int duration;
    @JsonProperty("BeginDay")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate beginDay;
    @JsonProperty("EndDay")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDay;
    @JsonProperty("StartTimes")
    @JsonFormat(pattern = "HH:mm")
    private List<LocalTime> startTimes;
}

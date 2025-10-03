package com.team7.ticket_booth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalTime;

@Data
public class PriceRequestDTO {
    @JsonProperty("type")
    private String type;
    @JsonProperty("beginTime")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime beginTime;
    @JsonProperty("value")
    private int value;
}

package com.team7.ticket_booth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HallRequestDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("row")
    private int row;
    @JsonProperty("column")
    private int column;
}

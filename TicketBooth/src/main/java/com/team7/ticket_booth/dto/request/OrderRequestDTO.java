package com.team7.ticket_booth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDTO {
    @JsonProperty("UserId")
    private UUID userId;
    @JsonProperty("ShowId")
    private UUID showId;
    @JsonProperty("TicketIds")
    private List<UUID> ticketIds;
    @JsonProperty("Method")
    private String method;
}

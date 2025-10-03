package com.team7.ticket_booth.dto.response;

import com.team7.ticket_booth.model.Hall;
import lombok.Data;

import java.util.UUID;

@Data
public class HallResponseDTO {
    private UUID id;
    private String name;
    private int row;
    private int column;

    public HallResponseDTO(Hall hall) {
        this.id = hall.getId();
        this.name = hall.getName();
        this.row = hall.getRow();
        this.column = hall.getColumn();
    }
}

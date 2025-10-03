package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.HallRequestDTO;
import com.team7.ticket_booth.dto.response.HallResponseDTO;
import com.team7.ticket_booth.exception.RequestException;
import com.team7.ticket_booth.model.Hall;
import com.team7.ticket_booth.model.Seat;
import com.team7.ticket_booth.model.Show;
import com.team7.ticket_booth.model.Ticket;
import com.team7.ticket_booth.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Array2DHashSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HallService {

    private final HallRepository hallRepository;

    public HallResponseDTO getHallByName(String name) {
        Hall hall = hallRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Hall not found with name: " + name));
        return new HallResponseDTO(hall);
    }


    public List<HallResponseDTO> getAllHalls() {
        List<Hall> halls = hallRepository.findAll();
        return halls.stream().map(HallResponseDTO::new).toList();
    }

    public HallResponseDTO createHall(HallRequestDTO dto) {
        if (hallRepository.findByName(dto.getName()).isPresent()) {
            throw new RequestException("Hall already exists");
        }
        Hall hall = new Hall(null, dto.getName(), dto.getRow(), dto.getColumn(), new HashSet<Show>(), new HashSet<Seat>());
        return new HallResponseDTO(hallRepository.save(hall));
    }

    public HallResponseDTO updateHall(HallRequestDTO dto) {
        Hall hall = hallRepository.findByName(dto.getName())
                .orElseThrow(() -> new RequestException("Hall not found"));
        hall.setName(dto.getName());
        hall.setRow(dto.getRow());
        hall.setColumn(dto.getColumn());
        return new HallResponseDTO(hallRepository.save(hall));
    }

    public void deleteHall(HallRequestDTO dto) {
        Hall hall = hallRepository.findByName(dto.getName())
                .orElseThrow(() -> new RequestException("Hall not found"));
        hallRepository.delete(hall);
    }
}


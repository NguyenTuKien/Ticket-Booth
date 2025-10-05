package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.HallRequestDTO;
import com.team7.ticket_booth.dto.request.SeatRequestDTO;
import com.team7.ticket_booth.dto.response.HallResponseDTO;
import com.team7.ticket_booth.dto.response.SeatResponseDTO;
import com.team7.ticket_booth.exception.NotFoundException;
import com.team7.ticket_booth.model.Hall;
import com.team7.ticket_booth.model.Seat;
import com.team7.ticket_booth.model.enums.SeatType;
import com.team7.ticket_booth.repository.HallRepository;
import com.team7.ticket_booth.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;

    public SeatResponseDTO getSeatByRowAndColumn(char row, int column) {
        String position = String.valueOf(row) + String.format("%02d", column);
        Seat seat = seatRepository.findByPosition(position)
                .orElseThrow(() -> new RuntimeException("Seat not found with position: " + position));
        return new SeatResponseDTO(seat);
    }

    public List<SeatResponseDTO> getSeatBySeatType(SeatType seatType) {
        return seatRepository.findBySeatType(seatType).stream()
                .map(SeatResponseDTO::new)
                .toList();
    }

    public List<SeatResponseDTO> getSeatsByHallName(String hallName) {
        UUID hallId = hallRepository.findHallIdByName(hallName)
                .orElseThrow(() -> new NotFoundException("Hall not found with name: " + hallName));
        return seatRepository.findByHallId(hallId).stream()
                .map(SeatResponseDTO::new)
                .toList();
    }

    public void createSeat(HallResponseDTO dto) {
        Hall hall = hallRepository.findByName(dto.getName())
                .orElseThrow(() -> new NotFoundException("Hall not found"));
        for (int i = 1; i < dto.getColumn(); ++i){
            for (char j = 'A'; j < dto.getRow() + 'A'; ++j){
                String pos = String.format("%c%02d", j, i);
                SeatType tos = SeatType.NORMAL;
                if(j == 'J') tos = SeatType.COUPLE;
                else if(j >= 'C' & j <= 'G' & i >= 3 & i <= 10) tos = SeatType.VIP;
                seatRepository.save(Seat.builder().position(pos).hall(hall).seatType(tos).build());
            }
        }
    }

    public SeatResponseDTO updateSeat(UUID id, SeatRequestDTO dto) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seat not found"));
        seat.setSeatType(dto.getSeatType());
        return new SeatResponseDTO(seatRepository.save(seat));
    }

    public void updateAllSeatInHall(HallResponseDTO dto) {
        List<Seat> seats = seatRepository.findByHallId(dto.getId());
        seatRepository.deleteAll(seats);
        createSeat(dto);
    }

    public void deleteSeat(UUID id) {
        if(!seatRepository.existsById(id)) throw new NotFoundException("Seat not found");
        seatRepository.deleteById(id);
    }

    public List<SeatResponseDTO> getSeatsByIds(List<UUID> seatIds) {
        List<Seat> seats = seatRepository.findAllById(seatIds);
        return seats.stream()
                .map(SeatResponseDTO::new)
                .toList();
    }

    public SeatResponseDTO getSeatById(UUID seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new NotFoundException("Seat not found with id: " + seatId));
        return new SeatResponseDTO(seat);
    }

    public List<SeatResponseDTO> getAvailableSeatsForShow(UUID showId) {
        // Lấy ghế chưa được đặt cho suất chiếu này
        List<Seat> availableSeats = seatRepository.findAvailableSeatsForShow(showId);
        return availableSeats.stream()
                .map(SeatResponseDTO::new)
                .toList();
    }

    public List<SeatResponseDTO> getBookedSeatsForShow(UUID showId) {
        // Lấy ghế đã được đặt cho suất chiếu này
        List<Seat> bookedSeats = seatRepository.findBookedSeatsForShow(showId);
        return bookedSeats.stream()
                .map(SeatResponseDTO::new)
                .toList();
    }
}

package com.team7.ticket_booth.config;

import com.team7.ticket_booth.model.enums.TypeOfSeat;
import com.team7.ticket_booth.dao.HallRepository;
import com.team7.ticket_booth.dao.SeatRepository;
import com.team7.ticket_booth.model.Hall;
import com.team7.ticket_booth.model.Seat;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInit {
    @Bean
    @Order(1)
    CommandLineRunner initHall(HallRepository hallRepository){
        return args -> {
            if (hallRepository.count() == 0) {
                hallRepository.saveAll(List.of(
                        Hall.builder().name("Phòng 1").numberOfSeats(120).build(),
                        Hall.builder().name("Phòng 2").numberOfSeats(120).build(),
                        Hall.builder().name("Phòng 3").numberOfSeats(120).build(),
                        Hall.builder().name("Phòng 4").numberOfSeats(120).build(),
                        Hall.builder().name("Phòng 5").numberOfSeats(120).build()
                ));
            }
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner initSeat(HallRepository hallRepository, SeatRepository seatRepository){
        return args -> {
            if(seatRepository.count() == 0){
                List<Hall> halls = hallRepository.findAll();
                for (Hall hall : halls) {
                    List<Seat> seats = new ArrayList<>();
                    for (char i = 'A'; i <= 'J'; i++) {      // 10 hàng
                        for (int j = 1; j <= 12; j++) {      // 12 ghế/hàng
                            String pos = String.format("%c%02d", i, j);
                            TypeOfSeat tos = TypeOfSeat.NORMAL;
                            if(i == 'J') tos = TypeOfSeat.COUPLE;
                            else if(i >= 'C' & i <= 'G' & j >= 3 & j <= 10) tos = TypeOfSeat.VIP;
                            seats.add(Seat.builder().position(pos).hall(hall).typeOfSeat(tos).build());
                        }
                    }
                    seatRepository.saveAll(seats); // insert batch 120 seat cho 1 hall
                }
            }
        };
    }
}


package com.team7.ticket_booth.config;

import com.team7.ticket_booth.model.Price;
import com.team7.ticket_booth.model.enums.SeatType;
import com.team7.ticket_booth.model.enums.Shift;
import com.team7.ticket_booth.repository.HallRepository;
import com.team7.ticket_booth.repository.PriceRepository;
import com.team7.ticket_booth.repository.SeatRepository;
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
                        Hall.builder().name("Phòng 1").row(10).column(12).build(),
                        Hall.builder().name("Phòng 2").row(10).column(12).build(),
                        Hall.builder().name("Phòng 3").row(10).column(12).build(),
                        Hall.builder().name("Phòng 4").row(10).column(12).build(),
                        Hall.builder().name("Phòng 5").row(10).column(12).build()
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
                            SeatType tos = SeatType.NORMAL;
                            if(i == 'J') tos = SeatType.COUPLE;
                            else if(i >= 'C' && i <= 'G' && j >= 3 && j <= 10) tos = SeatType.VIP;
                            seats.add(Seat.builder().position(pos).hall(hall).seatType(tos).build());
                        }
                    }
                    seatRepository.saveAll(seats); // insert batch 120 seat cho 1 hall
                }
            }
        };
    }

    @Bean
    @Order(3)
    CommandLineRunner initPrice(PriceRepository priceRepository) {
        return args -> {
            if(priceRepository.count() == 0) {
                List<Price> prices = new ArrayList<>();
                for ( SeatType seatType : SeatType.values()){
                    for ( Shift shift : Shift.values()){
                        Price price = new Price(null, seatType, shift, 80000);
                        prices.add(price);
                    }
                }
                priceRepository.saveAll(prices);
            }
        };
    }

}


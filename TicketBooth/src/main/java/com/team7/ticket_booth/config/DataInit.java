package com.team7.ticket_booth.config;

import com.team7.ticket_booth.model.Price;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.model.enums.Role;
import com.team7.ticket_booth.model.enums.SeatType;
import com.team7.ticket_booth.model.enums.Shift;
import com.team7.ticket_booth.repository.HallRepository;
import com.team7.ticket_booth.repository.PriceRepository;
import com.team7.ticket_booth.repository.SeatRepository;
import com.team7.ticket_booth.model.Hall;
import com.team7.ticket_booth.model.Seat;
import com.team7.ticket_booth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.repository.Repository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class DataInit {
    @Bean
    @Order(1)
    CommandLineRunner initUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()) {
                User user = new User().builder()
                        .username("admin")
                        .password(passwordEncoder.encode("Team7#D23CT"))
                        .email("admin@localhost.vn")
                        .fullName("Admin")
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(user);
            }

        };
    }

    @Bean
    @Order(2)
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
    @Order(3)
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
    @Order(4)
    CommandLineRunner initPrice(PriceRepository priceRepository) {
        return args -> {
            if(priceRepository.count() == 0) {
                List<Price> prices = new ArrayList<>();
                for ( SeatType seatType : SeatType.values()){
                    for ( Shift shift : Shift.values()){
                        Price price;
                        if(seatType == SeatType.VIP && (shift == Shift.EVENING || shift == Shift.LATE_EVENING)){
                            price = new Price(null, seatType, shift, 75000);
                        } else if (seatType == SeatType.COUPLE && (shift == Shift.LATE_EVENING || shift == Shift.EVENING)) {
                            price = new Price(null, seatType, shift, 55000);
                        } else if (shift == Shift.EVENING || shift == Shift.LATE_EVENING) {
                            price = new Price(null, seatType, shift, 65000);
                        } else if (seatType == SeatType.VIP) {
                            price = new Price(null, seatType, shift, 70000);
                        } else if (seatType == SeatType.COUPLE) {
                            price = new Price(null, seatType, shift, 50000);
                        } else {
                            price = new Price(null, seatType, shift, 60000);
                        }
                        prices.add(price);
                    }
                }
                priceRepository.saveAll(prices);
            }
        };
    }

}

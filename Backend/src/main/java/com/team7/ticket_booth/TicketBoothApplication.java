package com.team7.ticket_booth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicketBoothApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketBoothApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(TicketBoothApplication application) {
        return runner -> {
            System.out.println("Welcome to Ticket Booth !!!");
        };
    }
}


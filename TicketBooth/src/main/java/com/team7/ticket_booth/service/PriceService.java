package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.PriceRequestDTO;
import com.team7.ticket_booth.exception.RequestException;
import com.team7.ticket_booth.model.Price;
import com.team7.ticket_booth.model.enums.SeatType;
import com.team7.ticket_booth.model.enums.Shift;
import com.team7.ticket_booth.repository.PriceRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@RequiredArgsConstructor
public class PriceService {
    private final PriceRepository priceRepository;

    @Transactional
    public void updatePrice(PriceRequestDTO dto){
        Shift shift = (dto.getBeginTime() != null) ? Shift.findByBeginTime(dto.getBeginTime()) : null;
        SeatType seatType = (dto.getType() != null) ? SeatType.findByDescription(dto.getType()) : null;
        Price price;
        if(shift != null && seatType != null) {
            price = priceRepository.findByShiftAndSeatType(shift, seatType)
                    .orElseThrow(() -> new RequestException("Price not found for shift: " + shift + " and seat type: " + seatType));
        } else if(shift != null){
            price = priceRepository.findByShift(shift)
                    .orElseThrow(() -> new RequestException("Price not found for shift: " + shift));
        } else if(seatType != null){
            price = priceRepository.findBySeatType(seatType)
                    .orElseThrow(() -> new RequestException("Price not found for seat type: " + seatType));
        } else {
            throw new RequestException("Invalid request: both shift and seatType are null");
        }
        price.setValue(dto.getValue());
    }


    @Transactional
    public void incresePrice(PriceRequestDTO dto){
        Shift shift = (dto.getBeginTime() != null) ? Shift.findByBeginTime(dto.getBeginTime()) : null;
        SeatType seatType = (dto.getType() != null) ? SeatType.findByDescription(dto.getType()) : null;

        Price price;
        if(shift != null && seatType != null){
            price = priceRepository.findByShiftAndSeatType(shift, seatType)
                    .orElseThrow(() -> new RequestException("Price not found for shift: " + shift + " and seat type: " + seatType));
        }
        else if(shift != null){
            price = priceRepository.findByShift(shift)
                    .orElseThrow(() -> new RequestException("Price not found for shift: " + shift));
        }
        else if(seatType != null){
            price = priceRepository.findBySeatType(seatType)
                    .orElseThrow(() -> new RequestException("Price not found for seat type: " + seatType));
        }
        else{
            throw new RequestException("Invalid request: both shift and seatType are null");
        }

        price.setValue(price.getValue() + dto.getValue()); // Hibernate sẽ tự update khi commit
    }

}

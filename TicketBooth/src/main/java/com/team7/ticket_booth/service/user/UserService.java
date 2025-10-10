package com.team7.ticket_booth.service.user;

import com.team7.ticket_booth.dto.request.CardRequestDTO;
import com.team7.ticket_booth.exception.NotFoundException;
import com.team7.ticket_booth.model.Card;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.repository.CardRepository;
import com.team7.ticket_booth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public void updateCardInfo(CardRequestDTO cardRequestDTO) {
        User user = userRepository.findByEmail(cardRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Optional<Card> existingCard = cardRepository.findByUserId(user.getId());

        Card card;
        if (existingCard.isPresent()) {
            // Cập nhật card hiện có
            card = existingCard.get();
            card.setCardNumber(cardRequestDTO.getCardNumber());
            card.setHoldersName(cardRequestDTO.getHolderName());
            card.setExpirationDate(cardRequestDTO.getExpirationDate());
            card.setCvv(cardRequestDTO.getCvv());
        } else {
            card = new Card(null, user, cardRequestDTO.getCardNumber(), cardRequestDTO.getHolderName(),
                    cardRequestDTO.getExpirationDate(), cardRequestDTO.getCvv());
        }
        cardRepository.save(card);
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}

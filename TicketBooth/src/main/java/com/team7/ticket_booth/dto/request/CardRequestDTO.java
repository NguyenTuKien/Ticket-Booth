package com.team7.ticket_booth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team7.ticket_booth.model.enums.CardType;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CardRequestDTO {
    @JsonProperty("email")
    private String email;
    @JsonProperty("card_type")
    private CardType cardType;
    @JsonProperty("card_number")
    private String cardNumber;
    @JsonProperty("holder_name")
    private String holderName;
    @JsonProperty("expiration_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yyyy")
    private Date expirationDate;
    @JsonProperty("card_cvv")
    private String cvv;
}

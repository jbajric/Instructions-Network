package com.instructionnetwork.rezervacije.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ReservationCreatedEvent {
    private Integer reservation_id;
    private Integer appointment_id;
    private String token;
}

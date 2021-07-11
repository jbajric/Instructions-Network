package com.instructionnetwork.rezervacije.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class AppointmentFailedReservationEvent {
    private Integer reservation_id;
}

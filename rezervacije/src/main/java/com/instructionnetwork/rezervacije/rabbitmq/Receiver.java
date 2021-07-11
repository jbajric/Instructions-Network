package com.instructionnetwork.rezervacije.rabbitmq;

import com.instructionnetwork.rezervacije.repositories.ReservationsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    private ReservationsRepository reservationsRepository;

    @RabbitListener(queues = "APPOINTMENT_QUEUE")
    public void handleAppointmentFailedReservationEvent(AppointmentFailedReservationEvent event){
        if (event != null) {
            System.out.println("Finished rollback!");
            reservationsRepository.deleteById(event.getReservation_id());
        }
    }

}

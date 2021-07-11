package com.instructionnetwork.termini.rabbitmq;

import com.instructionnetwork.termini.repositories.AppointmentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class Receiver {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "RESERVATION_QUEUE")
    public void handleReservationCreatedEvent(ReservationCreatedEvent event){
        try {
            if (!appointmentRepository.existsById(event.getAppointment_id()))
                    throw new Exception("Appointment with ID " + event.getAppointment_id() + " doesn't exist!");
        }
        catch(Exception e) {
            System.out.println("Rollback needed! " + e.getMessage());
            AppointmentFailedReservationEvent event1 = new AppointmentFailedReservationEvent(event.getReservation_id());
            rabbitTemplate.convertAndSend("APPOINTMENT_EXCHANGE", "APPOINTMENT_ROUTING_KEY", event1);
        }
    }

}

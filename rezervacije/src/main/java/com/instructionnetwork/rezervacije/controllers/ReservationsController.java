package com.instructionnetwork.rezervacije.controllers;

import com.instructionnetwork.rezervacije.model.Reservations;
import com.instructionnetwork.rezervacije.rabbitmq.ReservationCreatedEvent;
import com.instructionnetwork.rezervacije.services.ReservationsServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservationsController {

    @Autowired
    private ReservationsServices reservationsServices;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    private Integer getStudentIdFromToken(String token) {
        String[] jwt = token.split(" ");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt[1]).getBody();
        return Integer.valueOf(claims.getSubject());
    }

    // --------------------------------------------------------------------------------
    // GET zahtjevi - public
    // --------------------------------------------------------------------------------

    @GetMapping("/allReservations")
    public ResponseEntity<List<Reservations>> getAllReservations() {
        return ResponseEntity.ok(reservationsServices.getAllReservationsService());
    }

    @GetMapping("/reservationsByStudent/{id}")
    public ResponseEntity<List<Reservations>> getReservationsForStudent(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(reservationsServices.getReservationsForStudentService(id));
    }
    @GetMapping("/reservationById/{id}")
    public ResponseEntity<Reservations> getReservationById(@PathVariable Integer id) {
        return ResponseEntity.ok(reservationsServices.getReservationByIdService(id));
    }

    // --------------------------------------------------------------------------------
    // POST zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @PostMapping(path = "/reservation", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Reservations> addReservation(@RequestBody Reservations reservation,
                                                       @RequestHeader("Authorization") String token) {

       if (!getStudentIdFromToken(token).equals(reservation.getStudent_id()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer newId = reservationsServices.addReservationsService(reservation);
        ReservationCreatedEvent event = new ReservationCreatedEvent(newId, reservation.getAppointment_id(), token);
        rabbitTemplate.convertAndSend("RESERVATION_EXCHANGE", "RESERVATION_ROUTING_KEY", event);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // PUT zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @PutMapping(path = "/reservation/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Reservations> updateReservation(@PathVariable("id") Integer id,
                                                          @RequestBody Reservations reservation,
                                                          @RequestHeader("Authorization") String token) {

        if (!getStudentIdFromToken(token).equals(reservation.getStudent_id()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        reservationsServices.updateReservationService(reservation, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // DELETE zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<Reservations> deleteReservation(@PathVariable("id") Integer id,
                                                          @RequestHeader("Authorization") String token) {

        ArrayList<Reservations> allReservations = (ArrayList<Reservations>) reservationsServices.getAllReservationsService();
        for(Reservations reservation : allReservations)
            if (reservation.getId().equals(id) && getStudentIdFromToken(token).equals(reservation.getStudent_id())) {
                reservationsServices.deleteReservationService(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}

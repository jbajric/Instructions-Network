package com.instructionnetwork.termini.controllers;

import com.instructionnetwork.termini.model.Appointment;
import com.instructionnetwork.termini.model.Instructors;
import com.instructionnetwork.termini.services.AppointmentService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
public class AppointmentController {

    private String getUsername(String token) {
        String[] jwt = token.split(" ");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt[1]).getBody();
        return String.valueOf(claims.get("Username"));
    }

    private String getBearerValue(String token) {
        String[] jwt = token.split(" ");
        return jwt[1];
    }

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private AppointmentService appointmentServices;
    @Autowired
    private RestTemplate restTemplate;

    // --------------------------------------------------------------------------------
    // GET zahtjevi - public
    // --------------------------------------------------------------------------------

    @GetMapping("/appointmentsByInstructor/{instructor_id}")
    public ResponseEntity<List<Appointment>> getAppointmentsByInstructor(@PathVariable("instructor_id") Integer instructorId) {
        return ResponseEntity.ok(appointmentServices.getAppointmentsByInstructorService(instructorId));
    }

    @GetMapping("/allAppointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentServices.getAllAppointmentsService());
    }

    @GetMapping("/appointment/{date}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable String date) {
        return ResponseEntity.ok(appointmentServices.getAppointmentService(date));
    }

    @GetMapping("/appointmentById/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentServices.getAppointmentByIdService(id));
    }

    // --------------------------------------------------------------------------------
    // POST zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"INSTRUCTOR"})
    @PostMapping(path = "/appointment", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment,
                                                      @RequestHeader ("Authorization") String token) {

        ResponseEntity<Instructors> response = restTemplate.getForEntity("http://korisnik/instructor/" + appointment.getInstructor_id(), Instructors.class);
        if (!response.getBody().getUsername().equals(getUsername(token)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        appointmentServices.addAppointmentService(appointment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // PUT zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"INSTRUCTOR", "STUDENT"})
    @PutMapping(path = "/appointment/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable("id") Integer id,
                                                         @RequestBody Appointment appointment,
                                                         @RequestHeader ("Authorization") String token) {

        appointmentServices.updateAppointmentService(appointment, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // DELETE zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"INSTRUCTOR"})
    @DeleteMapping("/appointmentsByInstructor/{instructor_id}")
    public ResponseEntity<List<Appointment>> deleteAppointmentsByInstructor(@PathVariable("instructor_id") Integer instructorId,
                                                                            @RequestHeader ("Authorization") String token) {

        ResponseEntity<Instructors> response = restTemplate.getForEntity("http://korisnik/instructor/" + instructorId, Instructors.class);
        if (!response.getBody().getUsername().equals(getUsername(token)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        appointmentServices.deleteAppointmentsByInstructorService(instructorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RolesAllowed({"INSTRUCTOR"})
    @DeleteMapping("/appointment/{id}")
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable("id") Integer id,
                                                         @RequestHeader ("Authorization") String token) {

        Appointment appointment = appointmentServices.getAppointmentByIdService(id);
        ResponseEntity<Instructors> response = restTemplate.getForEntity("http://korisnik/instructor/" + appointment.getInstructor_id(), Instructors.class);
        if (!response.getBody().getUsername().equals(getUsername(token)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        appointmentServices.deleteAppointmentService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

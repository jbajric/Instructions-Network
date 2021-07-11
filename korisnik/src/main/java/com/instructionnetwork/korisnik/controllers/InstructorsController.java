package com.instructionnetwork.korisnik.controllers;

import com.instructionnetwork.korisnik.model.*;
import com.instructionnetwork.korisnik.projections.InstructorsProjection;
import com.instructionnetwork.korisnik.services.InstructorsServices;
import com.instructionnetwork.korisnik.services.StudentsServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
public class InstructorsController {

    private String getUsername(String token) {
        String[] jwt = token.split(" ");
        Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(jwt[1]).getBody();
        return String.valueOf(claims.get("Username"));
    }

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private InstructorsServices instructorServices;
    @Autowired
    private StudentsServices studentsServices;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // --------------------------------------------------------------------------------
    // AUTH i REGISTER - public
    // --------------------------------------------------------------------------------

    @GetMapping("/instructors/authusername/{username}")
    public Instructors getInstructorByUsername(@PathVariable("username") String username) {
        return instructorServices.getInstructorByUsernameService(username);
    }

    @GetMapping("/instructors/authid/{id}")
    public Instructors getInstructorById(@PathVariable("id") Integer id) {
        return instructorServices.getInstructorByIdService(id);
    }

    @PostMapping(path = "/registerInstructor", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Instructors> addInstructor(@Valid @RequestBody Instructors instructor) throws Exception {
        instructor.setPassword(passwordEncoder.encode(instructor.getPassword()));
        instructor.setRoles(new HashSet<>(Arrays.asList(new Role[]{new Role(1L, RoleName.INSTRUCTOR)})));
        List<Instructors> inst = instructorServices.getAllInstructorsService();
        List<Students> students = studentsServices.getAllStudentsService();
        for (Instructors in : inst)
            if (in.getUsername().equals(instructor.getUsername()))
                throw new Exception("Username already taken!");
        for (Students s : students)
            if (s.getUsername().equals(instructor.getUsername()))
                throw new Exception("Username already taken!");
        instructorServices.addInstructorService(instructor);
        return ResponseEntity.ok(instructorServices.getInstructorByUsernameService(instructor.getUsername()));
    }

    // --------------------------------------------------------------------------------
    // GET zahtjevi - public
    // --------------------------------------------------------------------------------

    @GetMapping("/instructor/{id}")
    public ResponseEntity<Instructors> getInstructor(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(instructorServices.getInstructorService(id));
    }

    @GetMapping("/allInstructors")
    public ResponseEntity<List<Instructors>> getAllInstructors() {
        return ResponseEntity.ok(instructorServices.getAllInstructorsService());
    }

    @GetMapping("/allInstructorsWithAverageRating")
    public ResponseEntity<List<InstructorsProjection>> getAllInstructorsWithAverageRating() {
        return ResponseEntity.ok(instructorServices.getAllInstructorsWithAverageRatingService());
    }

    @GetMapping("/instructorsByStudent/{id}")
    public ResponseEntity<List<Instructors>> getInstructorsForStudent(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(instructorServices.getAllInstructorsByStudentService(id));
    }

    @GetMapping("/instructor/appointments/{id}")
    public List<Appointment> getAppointmentsForInstructor(@PathVariable("id") Integer id) {
        ResponseEntity<Appointment[]> response = restTemplate.getForEntity("http://termini/appointmentsByInstructor/" + id, Appointment[].class);
        return Arrays.asList(response.getBody());
    }

    @GetMapping("/instructorByName/{name}")
    public ResponseEntity<Instructors> getInstructorByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(instructorServices.getInstructorByNameService(name));
    }

    // --------------------------------------------------------------------------------
    // POST zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"INSTRUCTOR"})
    @PostMapping(path = "/instructor/appointments", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Appointment> createAppointmentsForInstructor(@RequestBody Appointment appointment,
                                                                       @RequestHeader ("Authorization") String token) {

        if (instructorServices.getInstructorByUsernameService(getUsername(token)) == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", token);
        restTemplate.exchange("http://termini/appointment",
                                    HttpMethod.POST,
                                    new HttpEntity<>(appointment, httpHeaders),
                                    Appointment.class);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // PUT zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"INSTRUCTOR"})
    @PutMapping(path = "/instructor/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Instructors> updateInstructors(@PathVariable("id") Integer id,
                                                         @Valid @RequestBody Instructors instructor,
                                                         @RequestHeader ("Authorization") String token) {

        if (instructorServices.getInstructorByUsernameService(getUsername(token)) == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        instructorServices.updateInstructorService(instructor, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // DELETE zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"INSTRUCTOR"})
    @DeleteMapping("instructor/appointments/{id}")
    public ResponseEntity<Instructors> deleteAppointmentsForInstructor(@PathVariable("id") Integer id,
                                                                       @RequestHeader ("Authorization") String token) {

        if (instructorServices.getInstructorByUsernameService(getUsername(token)) == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", token);
        restTemplate.exchange("http://termini/appointmentsByInstructor/" + id,
                                HttpMethod.DELETE,
                                new HttpEntity<>(httpHeaders),
                                Void.class);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RolesAllowed({"INSTRUCTOR"})
    @DeleteMapping(value = "/instructor/{id}")
    public ResponseEntity<Instructors> deleteInstructor(@PathVariable("id") Integer id,
                                                        @RequestHeader ("Authorization") String token) {

        if (instructorServices.getInstructorByUsernameService(getUsername(token)) == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        instructorServices.deleteInstructorService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package com.instructionnetwork.korisnik.controllers;

import com.instructionnetwork.korisnik.model.InstructorsRatings;
import com.instructionnetwork.korisnik.services.InstructorsRatingsServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;

@RestController
public class InstructorsRatingsController {

    private Integer getStudentIdFromToken(String token) {
        String[] jwt = token.split(" ");
        Claims claims = Jwts.parser()
                            .setSigningKey(jwtSecret)
                            .parseClaimsJws(jwt[1]).getBody();
        return Integer.valueOf(claims.getSubject());
    }

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private InstructorsRatingsServices instructorsRatingsServices;

    // --------------------------------------------------------------------------------
    // GET zahtjevi - public
    // --------------------------------------------------------------------------------

    @GetMapping("/rating/{instructor_id}")
    public ResponseEntity<ArrayList<InstructorsRatings>> getRatingsByInstructor(@Valid @PathVariable("instructor_id") Integer instructor_id) {
        return ResponseEntity.ok(instructorsRatingsServices.getRatingsByInstructorService(instructor_id));
    }

    @GetMapping("/allRatings")
    public ResponseEntity<ArrayList<InstructorsRatings>> getAllRatings() {
        return ResponseEntity.ok(instructorsRatingsServices.getAllRatingsService());
    }

    @GetMapping("/averageRating/{instructor_id}")
    public ResponseEntity<Double> getInstructorsAvgRating(@PathVariable("instructor_id") Integer id) {
        return ResponseEntity.ok(instructorsRatingsServices.getInstructorsAverageRatingService(id));
    }

    // --------------------------------------------------------------------------------
    // PUT zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @PostMapping(path = "/rating", consumes = "application/json", produces = "application/json")
    public ResponseEntity<InstructorsRatings> addRatings(@Valid @RequestBody InstructorsRatings ir,
                                                         @RequestHeader("Authorization") String token) {
        if (!ir.getStudents().getId().equals(getStudentIdFromToken(token)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        instructorsRatingsServices.addRatingService(ir);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // PUT zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @PutMapping(path = "/ratings/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<InstructorsRatings> updateRatings(@PathVariable("id") Integer id,
                                                            @Valid @RequestBody InstructorsRatings ir,
                                                            @RequestHeader("Authorization") String token) {
        if (!ir.getStudents().getId().equals(getStudentIdFromToken(token)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        instructorsRatingsServices.updateRatingService(id, ir);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // DELETE zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @DeleteMapping(value = "/ratings/{id}")
    public ResponseEntity<InstructorsRatings> deleteRating(@PathVariable("id") Integer id,
                                                           @RequestHeader("Authorization") String token) {
        ArrayList<InstructorsRatings> allRatings = instructorsRatingsServices.getAllRatingsService();
        for(InstructorsRatings ir : allRatings)
            if (ir.getId().equals(id) && ir.getStudents().getId().equals(getStudentIdFromToken(token))) {
                instructorsRatingsServices.deleteRatingService(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
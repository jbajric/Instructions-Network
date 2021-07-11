package com.instructionnetwork.raspored.controllers;

import com.instructionnetwork.raspored.model.FreeTime;
import com.instructionnetwork.raspored.services.FreeTimeService;
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
import java.util.List;

@RestController
public class FreeTimeController {

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
    private FreeTimeService freeTimeService;

    // --------------------------------------------------------------------------------
    // GET zahtjevi - public
    // --------------------------------------------------------------------------------

    @GetMapping("/freeTimesByStudent/{student_id}")
    public ResponseEntity<List<FreeTime>> getFreeTimesByStudent(@PathVariable("student_id") Integer studentId) {
        return ResponseEntity.ok(freeTimeService.getFreeTimesByStudentService(studentId));
    }

    @GetMapping("/freeTime/{id}")
    public ResponseEntity<FreeTime> getFreeTime(@Valid @PathVariable Integer id) {
        return ResponseEntity.ok(freeTimeService.getFreeTimeService(id));
    }

    @GetMapping("/allFreeTimes")
    public ResponseEntity<List<FreeTime>> getAllFreeTimes() {
        return ResponseEntity.ok(freeTimeService.getAllFreeTimesService());
    }

    // --------------------------------------------------------------------------------
    // POST zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @PostMapping(path = "/freeTime", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FreeTime> addFreeTime(@Valid @RequestBody FreeTime freeTime,
                                                @RequestHeader ("Authorization") String token) {

        if (!getStudentIdFromToken(token).equals(freeTime.getStudent_id()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        freeTimeService.addFreeTimeService(freeTime);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // PUT zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @PutMapping(path = "/freeTime/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FreeTime> updateFreeTime(@PathVariable("id") Integer id, @Valid @RequestBody FreeTime freeTime,
                                                   @RequestHeader ("Authorization") String token) {

        if (!getStudentIdFromToken(token).equals(freeTime.getStudent_id()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        freeTimeService.updateFreeTimeService(freeTime, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // DELETE zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @DeleteMapping(value = "/freeTime/{id}")
    public ResponseEntity<FreeTime> deleteFreeTime(@Valid @PathVariable Integer id,
                                                   @RequestHeader ("Authorization") String token) {

        List<FreeTime> allFreeTimes = freeTimeService.getAllFreeTimesService();
        for (FreeTime fr : allFreeTimes)
            if (fr.getId().equals(id))
                if (fr.getStudent_id().equals(getStudentIdFromToken(token))) {
                    freeTimeService.deleteFreeTimeService(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}

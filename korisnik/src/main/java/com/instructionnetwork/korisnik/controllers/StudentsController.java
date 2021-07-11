package com.instructionnetwork.korisnik.controllers;

import com.instructionnetwork.korisnik.model.*;
import com.instructionnetwork.korisnik.services.InstructorsServices;
import com.instructionnetwork.korisnik.services.StudentsServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.IntToLongFunction;

@RestController
public class StudentsController {

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
    private StudentsServices studentsServices;
    @Autowired
    private InstructorsServices instructorServices;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // --------------------------------------------------------------------------------
    // AUTH i REGISTER - public
    // --------------------------------------------------------------------------------

    @GetMapping("/students/authusername/{username}")
    public Students getStudentByUsername(@PathVariable("username") String username) {
        return studentsServices.getStudentByUsernameService(username);
    }

    @GetMapping("/students/authid/{id}")
    public Students getStudentById(@PathVariable("id") Integer id) {
        return studentsServices.getStudentByIdService(id);
    }

    @PostMapping(path = "/registerStudent", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Students> addStudent(@Valid @RequestBody Students student) throws Exception {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setRoles(new HashSet<>(Arrays.asList(new Role[]{new Role(2L, RoleName.STUDENT)})));
        List<Instructors> inst = instructorServices.getAllInstructorsService();
        List<Students> students = studentsServices.getAllStudentsService();
        for (Instructors in : inst)
            if (in.getUsername().equals(student.getUsername()))
                throw new Exception("Username already taken!");
        for (Students s : students)
            if (s.getUsername().equals(student.getUsername()))
                throw new Exception("Username already taken!");
        studentsServices.addStudentService(student);
        return ResponseEntity.ok(studentsServices.getStudentByUsernameService(student.getUsername()));
    }

    // --------------------------------------------------------------------------------
    // GET zahtjevi - public
    // --------------------------------------------------------------------------------

    @GetMapping("/student/{id}")
    public ResponseEntity<Students> getStudent(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(studentsServices.getStudentService(id));
    }

    @GetMapping("/allStudents")
    public ResponseEntity<List<Students>> getAllStudents() {
        return ResponseEntity.ok(studentsServices.getAllStudentsService());
    }

    @GetMapping("/student/freetimes/{id}")
    public List<FreeTime> getFreeTimesForStudent(@PathVariable("id") Integer id) {
        ResponseEntity<FreeTime[]> response = restTemplate.getForEntity(
                                                                    "http://raspored/freeTimesByStudent/" + id,
                                                                        FreeTime[].class);
        return Arrays.asList(response.getBody());
    }

    // --------------------------------------------------------------------------------
    // PUT zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @PutMapping(path = "/student/{id}",consumes = "application/json", produces = "application/json")
    public ResponseEntity<Students> updateStudent(@PathVariable int id, @Valid @RequestBody Students student,
                                                  @RequestHeader ("Authorization") String token) {

        if (studentsServices.getStudentByUsernameService(getUsername(token)) == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        studentsServices.updateStudentService(student, id);
        return new ResponseEntity(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // DELETE zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"STUDENT"})
    @DeleteMapping("/student/{id}")
    public ResponseEntity<Students> deleteStudent(@PathVariable("id") Integer id,
                                                  @RequestHeader ("Authorization") String token) {

        if (studentsServices.getStudentByUsernameService(getUsername(token)) == null)
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        studentsServices.deleteStudentService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

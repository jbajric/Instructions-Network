package com.instructionnetwork.korisnik.controllers;

import com.instructionnetwork.korisnik.model.Subjects;
import com.instructionnetwork.korisnik.services.InstructorsServices;
import com.instructionnetwork.korisnik.services.StudentsServices;
import com.instructionnetwork.korisnik.services.SubjectsServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
public class SubjectsController {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private SubjectsServices subjectsServices;
    @Autowired
    private InstructorsServices instructorServices;
    @Autowired
    private StudentsServices studentsServices;

    private String getUsername(String token) {
        String[] jwt = token.split(" ");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt[1]).getBody();
        return String.valueOf(claims.get("Username"));
    }

    // --------------------------------------------------------------------------------
    // GET zahtjevi - public
    // --------------------------------------------------------------------------------

    @GetMapping("/subject/{id}")
    public ResponseEntity<Subjects> getSubject(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(subjectsServices.getSubjectService(id));
    }

    @GetMapping("/allSubjects")
    public ResponseEntity<List<Subjects>> getAllSubjects() {
        return ResponseEntity.ok(subjectsServices.getAllSubjectsService());
    }

    @GetMapping("/allSubjectNames")
    public ResponseEntity<List<String>> getAllSubjectsNames(){
        return ResponseEntity.ok(subjectsServices.getAllSubjectNamesService());
    }

    @GetMapping("/subjectsByStudent/{id}")
    public ResponseEntity<List<Subjects>> getStudentsSubjects(@PathVariable("id") Integer id){
        return ResponseEntity.ok(subjectsServices.getStudentsBySubjectService(id));
    }

    @GetMapping("/subjectsByInstructor/{id}")
    public ResponseEntity<List<Subjects>> getInstructorsSubjects(@PathVariable("id") Integer id){
        return ResponseEntity.ok(subjectsServices.getInstructorsBySubjectService(id));
    }

    // --------------------------------------------------------------------------------
    // POST zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"INSTRUCTOR"})
    @PostMapping(path = "/subject", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Subjects> addSubject(@Valid @RequestBody Subjects subject,
                                               @RequestHeader ("Authorization") String token) {

        if (instructorServices.getInstructorByUsernameService(getUsername(token)) == null)
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        subjectsServices.addSubjectService(subject);
        return new ResponseEntity(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // PUT zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"INSTRUCTOR", "STUDENT"})
    @PutMapping(path = "/subject/{id}",consumes = "application/json", produces = "application/json")
    public ResponseEntity<Subjects> updateSubject(@PathVariable int id,
                                                  @Valid @RequestBody Subjects subject,
                                                  @RequestHeader ("Authorization") String token) {

        if (instructorServices.getInstructorByUsernameService(getUsername(token)) == null || studentsServices.getStudentByUsernameService(getUsername(token)) == null)
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        subjectsServices.updateSubjectService(subject, id);
        return new ResponseEntity(HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------
    // DELETE zahtjevi - protected
    // --------------------------------------------------------------------------------

    @RolesAllowed({"INSTRUCTOR"})
    @DeleteMapping("/subject/{id}")
    public ResponseEntity<Subjects> deleteSubject(@PathVariable("id") Integer id,
                                                  @RequestHeader ("Authorization") String token){

        if (instructorServices.getInstructorByUsernameService(getUsername(token)) == null)
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        subjectsServices.deleteSubjectService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

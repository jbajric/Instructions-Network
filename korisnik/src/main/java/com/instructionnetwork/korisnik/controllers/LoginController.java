package com.instructionnetwork.korisnik.controllers;

import com.instructionnetwork.korisnik.exceptions.UserNotFoundException;
import com.instructionnetwork.korisnik.model.Instructors;
import com.instructionnetwork.korisnik.model.LoginRequest;
import com.instructionnetwork.korisnik.model.Role;
import com.instructionnetwork.korisnik.model.Students;
import com.instructionnetwork.korisnik.repositories.InstructorsRepository;
import com.instructionnetwork.korisnik.repositories.RolesRepository;
import com.instructionnetwork.korisnik.repositories.StudentsRepository;
import com.instructionnetwork.korisnik.security.JwtAuthenticationResponse;
import com.instructionnetwork.korisnik.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    InstructorsRepository instructorsRepository;
    @Autowired
    StudentsRepository studentsRepository;
    @Autowired
    RolesRepository roleRepository;
    @Autowired
    JwtTokenProvider tokenProvider;

    final
    PasswordEncoder passwordEncoder;

    public LoginController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt;
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if (instructorsRepository.findInstructorsByUsername(username) != null) {
            Instructors i = instructorsRepository.findInstructorsByUsername(username);
            if (passwordEncoder.matches(password, i.getPassword()))
                jwt = tokenProvider.generateToken(i.getId(), i.getUsername());
            else throw new UserNotFoundException("User (Instructor) with given credentials is not defined!");
        }
        else if (studentsRepository.findStudentsByUsername(username) != null) {
            Students s = studentsRepository.findStudentsByUsername(username);
            if (passwordEncoder.matches(password, s.getPassword()))
                jwt = tokenProvider.generateToken(s.getId(), s.getUsername());
            else throw new UserNotFoundException("User (Student) with given credentials is not defined!");
        }
        else throw new UserNotFoundException("User with given credentials is not defined!");
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @GetMapping("/allUsernames")
    public ArrayList<String> getAllUsernames() {
        ArrayList<String> allUsernames = new ArrayList<>();
        allUsernames.addAll(instructorsRepository.getAllUsernames());
        allUsernames.addAll(studentsRepository.getAllUsernames());
        return allUsernames;
    }

    @GetMapping("/allRoles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @GetMapping("/studentsRoles")
    public ResponseEntity<List<Integer>> getStudentsRoles() {
        return ResponseEntity.ok(roleRepository.getStudentsRoles());
    }

    @GetMapping("/instructorsRoles")
    public ResponseEntity<List<Integer>> getInstructorsRoles() {
        return ResponseEntity.ok(roleRepository.getInstructorsRoles());
    }
}

package com.instructionnetwork.termini.security;

import com.instructionnetwork.termini.exceptions.InstructorNotFoundException;
import com.instructionnetwork.termini.exceptions.StudentNotFoundException;
import com.instructionnetwork.termini.model.Instructors;
import com.instructionnetwork.termini.model.Students;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws InstructorNotFoundException, StudentNotFoundException {
        try {
            Instructors user = restTemplate.getForObject(
                    "http://localhost:8081/instructors/authusername/" + username,
                    Instructors.class);
            if (user == null)
                throw new InstructorNotFoundException("Instructor with the username '" + username + "' doesn't exist!");
            return UserPrincipal.create(user);
        } catch (InstructorNotFoundException e) {
            Students user = restTemplate.getForObject(
                    "http://localhost:8081/students/authusername/"+ username,
                    Students.class);
            if (user == null)
                throw new StudentNotFoundException("Student with the username '" + username + "' doesn't exist!");
            return UserPrincipal.create(user);
        }
    }

    @Transactional
    public UserDetails loadUserById(Long id) throws InstructorNotFoundException, StudentNotFoundException {
        try {
            Instructors user = restTemplate.getForObject(
                    "http://localhost:8081/instructors/authid/"+ id,
                    Instructors.class);
            if (user == null)
                throw new InstructorNotFoundException("Instructor with the ID '" + id + "' doesn't exist!");
            return UserPrincipal.create(user);
        }
        catch (InstructorNotFoundException e) {
            Students user = restTemplate.getForObject(
                    "http://localhost:8081/students/authid/"+ id,
                    Students.class);
            if (user == null)
                throw new StudentNotFoundException("Student with the ID '" + id + "' doesn't exist!");
            return UserPrincipal.create(user);
        }
    }

}

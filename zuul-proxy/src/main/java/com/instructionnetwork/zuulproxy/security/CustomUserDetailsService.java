package com.instructionnetwork.zuulproxy.security;

import com.instructionnetwork.zuulproxy.exceptions.InstructorNotFoundException;
import com.instructionnetwork.zuulproxy.exceptions.StudentNotFoundException;
import com.instructionnetwork.zuulproxy.model.Instructors;
import com.instructionnetwork.zuulproxy.model.Students;
import com.instructionnetwork.zuulproxy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws InstructorNotFoundException, StudentNotFoundException {
        try {
            ResponseEntity<Instructors> response = restTemplate.getForEntity("http://korisnik/instructor/" + username, Instructors.class);
            if (response == null)
                throw new InstructorNotFoundException("Instructor with the username '" + username + "' doesn't exist!");
            User user = response.getBody();
            return UserPrincipal.create(user);
        }
        catch (InstructorNotFoundException e) {
            ResponseEntity<Students> response = restTemplate.getForEntity("http://korisnik/student/" + username, Students.class);
            if (response == null)
                throw new StudentNotFoundException("Student with the username '\"" + username + "\"' doesn't exist!");
            User user = response.getBody();
            return UserPrincipal.create(user);
        }
    }

    @Transactional
    public UserDetails loadUserById(Long id) throws InstructorNotFoundException, StudentNotFoundException {
        try {
            ResponseEntity<Instructors> response = restTemplate.getForEntity("http://korisnik/instructor/" + id, Instructors.class);
            if (response == null)
                throw new InstructorNotFoundException("Instructor with the ID '\"" + id + "\"' doesn't exist!");
            User user = response.getBody();
            return UserPrincipal.create(user);
        }
        catch (InstructorNotFoundException e) {
            ResponseEntity<Students> response = restTemplate.getForEntity("http://korisnik/student/" + id, Students.class);
            if (response == null)
                throw new StudentNotFoundException("Instructor with the ID '\"" + id + "\"' doesn't exist!");
            User user = response.getBody();
            return UserPrincipal.create(user);
        }
    }

}

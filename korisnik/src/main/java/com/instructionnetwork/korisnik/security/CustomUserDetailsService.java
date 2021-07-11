package com.instructionnetwork.korisnik.security;

import com.instructionnetwork.korisnik.exceptions.InstructorNotFoundException;
import com.instructionnetwork.korisnik.exceptions.StudentNotFoundException;
import com.instructionnetwork.korisnik.model.User;
import com.instructionnetwork.korisnik.repositories.InstructorsRepository;
import com.instructionnetwork.korisnik.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    InstructorsRepository instructorsRepository;

    @Autowired
    StudentsRepository studentsRepository;

    public CustomUserDetailsService(InstructorsRepository instructorsRepository, StudentsRepository studentsRepository) {
        this.instructorsRepository = instructorsRepository;
        this.studentsRepository = studentsRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws InstructorNotFoundException, StudentNotFoundException {
        try {
            if (instructorsRepository.findInstructorsByUsername(username) == null)
                throw new InstructorNotFoundException("Instructor with the username '" + username + "' doesn't exist!");
            User user = instructorsRepository.findInstructorsByUsername(username);
            return UserPrincipal.create(user);
        }
        catch (InstructorNotFoundException e) {
            if (studentsRepository.findStudentsByUsername(username) == null)
                throw new StudentNotFoundException("Student with the username '\" + username + \"' doesn't exist!");
            User user = studentsRepository.findStudentsByUsername(username);
            return UserPrincipal.create(user);
        }
    }

    @Transactional
    public UserDetails loadUserById(Long id) throws InstructorNotFoundException, StudentNotFoundException {
        try {
            if (!instructorsRepository.existsById(Math.toIntExact(id)))
                throw new InstructorNotFoundException("Instructor with the ID '\" + id + \"' doesn't exist!");
            User user = instructorsRepository.findById(Math.toIntExact(id)).get();
            return UserPrincipal.create(user);
        }
        catch (InstructorNotFoundException e) {
            if (!studentsRepository.existsById(Math.toIntExact(id)))
                throw new StudentNotFoundException("Instructor with the ID '\" + id + \"' doesn't exist!");
            User user = studentsRepository.findById(Math.toIntExact(id)).get();
            return UserPrincipal.create(user);
        }
    }

}

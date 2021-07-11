package com.instructionnetwork.korisnik.services;

import com.instructionnetwork.korisnik.exceptions.InstructorNotFoundException;
import com.instructionnetwork.korisnik.exceptions.NoInstructorsDefinedException;
import com.instructionnetwork.korisnik.exceptions.NoInstructorsForStudentException;
import com.instructionnetwork.korisnik.model.Instructors;
import com.instructionnetwork.korisnik.projections.InstructorsProjection;
import com.instructionnetwork.korisnik.repositories.InstructorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("InstructorsServices")
public class InstructorsServices {

    @Autowired
    InstructorsRepository instructorsRepository;

    public Instructors getInstructorService(Integer id) {
        if (!instructorsRepository.existsById(id))
            throw new InstructorNotFoundException("Instructor with ID '" + id + "' doesn't exist!");
        return instructorsRepository.findById(id).get();
    }

    public List<Instructors> getAllInstructorsService() {
        List<Instructors> i = instructorsRepository.findAll();
        if (i.size() == 0) throw new NoInstructorsDefinedException("No instructors defined!");
        return i;
    }

    public void addInstructorService(Instructors instructor) {
        instructorsRepository.save(instructor);
    }

    public void updateInstructorService(Instructors instructor, Integer id) {
        Instructors oldInst = instructorsRepository.findById(id).get();
        oldInst.setFirst_name(instructor.getFirst_name());
        oldInst.setLast_name(instructor.getLast_name());
        oldInst.setEmail(instructor.getEmail());
        instructorsRepository.save(oldInst);
    }

    public void deleteInstructorService(Integer id) {
        boolean i = instructorsRepository.existsById(id);
        if (!i) throw new InstructorNotFoundException("Instructor with ID '" + id + "' doesn't exist!");
        instructorsRepository.removeInstructorsById(id);
    }

    public List<Instructors> getAllInstructorsByStudentService(Integer studentId) {
        List<Instructors> instructors = instructorsRepository.getInstructorsByStudentsSubjects(studentId);
        if (instructors.size() == 0)
            throw new NoInstructorsForStudentException("No instructors assigned!");
        return instructors;
    }

    public Instructors getInstructorByUsernameService(String username) {
        List<Instructors> st = instructorsRepository.findAll();
        for (Instructors i : st)
            if (i.getUsername().matches(username))
                return i;
        throw new InstructorNotFoundException("Instructor with username '" + username + "' doesn't exist!");
    }

    public Instructors getInstructorByIdService(Integer id) {
        if (!instructorsRepository.existsById(id))
            throw new InstructorNotFoundException("Instructor with ID '" + id + "' doesn't exist!");
        return instructorsRepository.findInstructorsById(id);
    }

    public List<InstructorsProjection> getAllInstructorsWithAverageRatingService() {
        List<InstructorsProjection> i = instructorsRepository.findAllInstructorsWithAverageRating();
        if (i.size() == 0) throw new NoInstructorsDefinedException("No instructors defined!");
        return i;
    }

    public Instructors getInstructorByNameService(String name) {
        String[] fullName = name.split("\\s+");
        String firstName = fullName[0];
        String lastName = fullName[1];
        return instructorsRepository.findInstructorsByFullName(firstName, lastName);
    }
}


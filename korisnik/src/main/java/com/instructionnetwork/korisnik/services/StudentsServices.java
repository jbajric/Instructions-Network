package com.instructionnetwork.korisnik.services;

import com.instructionnetwork.korisnik.exceptions.NoStudentsDefinedException;
import com.instructionnetwork.korisnik.exceptions.StudentNotFoundException;
import com.instructionnetwork.korisnik.model.Students;
import com.instructionnetwork.korisnik.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("StudentsServices")
public class StudentsServices {

    @Autowired
    StudentsRepository studentsRepository;

    public Students getStudentService(Integer id) {
        if (!studentsRepository.existsById(id)) throw new StudentNotFoundException("Student with ID '" + id + "' doesn't exist!");
        return studentsRepository.findById(id).get();
    }

    public Students getStudentByUsernameService(String username) {
        List<Students> st = studentsRepository.findAll();
        for (Students i : st)
            if (i.getUsername().matches(username))
                return i;
        throw new StudentNotFoundException("Student with username '" + username + "' doesn't exist!");
    }

    public Students getStudentByIdService(Integer id) {
        if (!studentsRepository.existsById(id))
            throw new StudentNotFoundException("Student with ID '" + id + "' doesn't exist!");
        return studentsRepository.findStudentsById(id);
    }

    public List<Students> getAllStudentsService() {
        List<Students> i = studentsRepository.findAll();
        if (i.size() == 0) throw new NoStudentsDefinedException("No students defined!");
        return i;
    }

    public void addStudentService(Students student) {
        studentsRepository.save(student);
    }

    public void updateStudentService(Students student, int id) {
        Students oldStudent = studentsRepository.findById(id).get();
        oldStudent.setFirst_name(student.getFirst_name());
        oldStudent.setLast_name(student.getLast_name());
        oldStudent.setEmail(student.getEmail());
        studentsRepository.save(oldStudent);
    }

    public void deleteStudentService(Integer id) {
        boolean i = studentsRepository.existsById(id);
        if (!i) throw new StudentNotFoundException("Student with ID '" + id + "' doesn't exist!");
        studentsRepository.removeStudentsById(id);
    }

}
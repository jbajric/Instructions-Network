package com.instructionnetwork.korisnik.services;

import com.instructionnetwork.korisnik.exceptions.*;
import com.instructionnetwork.korisnik.model.Subjects;
import com.instructionnetwork.korisnik.repositories.SubjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectsServices {

    @Autowired
    SubjectsRepository subjectsRepository;

    public Subjects getSubjectService(Integer id) {
        if (!subjectsRepository.existsById(id)) throw new SubjectNotFoundException("Subject with ID '" + id + "' doesn't exist!");
        return subjectsRepository.findById(id).get();
    }

    public List<Subjects> getAllSubjectsService() {
        List<Subjects> i = subjectsRepository.findAll();
        if (i.size() == 0) throw new NoSubjectsDefinedException("No subjects defined!");
        return i;
    }

    public void addSubjectService(Subjects subject) {
        subjectsRepository.save(subject);
    }

    public void updateSubjectService(Subjects subject, int id) {
        Subjects oldSubject = subjectsRepository.findById(id).get();
        oldSubject.setSubject_name(subject.getSubject_name());
        subjectsRepository.save(oldSubject);
    }

    public void deleteSubjectService(Integer id) {
        boolean i = subjectsRepository.existsById(id);
        if (!i) throw new SubjectNotFoundException("Subject with ID '" + id + "' doesn't exist!");
        subjectsRepository.removeSubjectsById(id);
    }

    public List<String> getAllSubjectNamesService() {
        List<String> subjectsNames = subjectsRepository.getSubjectsNames();
        if (subjectsNames.size() == 0) throw new NoSubjectsDefinedException("No subjects defined!");
        return subjectsNames;
    }

    public List<Subjects> getStudentsBySubjectService(Integer studentId) {
        List<Subjects> subjects = subjectsRepository.findSubjectsByStudentId(studentId);
        if (subjects.size() == 0) throw  new StudentsSubjectsNotFoundException("Student doesn't have any subjects!");
        return subjects;
    }

    public List<Subjects> getInstructorsBySubjectService(Integer instructorId) {
        List<Subjects> subjects = subjectsRepository.findSubjectsByInstructorId(instructorId);
        if (subjects.size() == 0) throw new InstructorsSubjectsNotFoundException("Instructor doesn't have any subjects!");
        return subjects;
    }

    public Subjects getSubjectByNameService(String subject_name) {
        Subjects subjects = subjectsRepository.findSubjectsByName(subject_name);
        if (subjects == null) throw new NoSubjectsDefinedException("No subjects defined!");
        return subjects;
    }
}

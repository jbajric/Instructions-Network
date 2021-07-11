package com.instructionnetwork.korisnik.repositories;

import com.instructionnetwork.korisnik.model.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubjectsRepository extends JpaRepository<Subjects, Integer> {

    @Query ("SELECT s from Subjects s")
    List<Subjects> findAll();

    @Transactional
    @Modifying
    @Query ("DELETE from Subjects s WHERE s.id=:id")
    void removeSubjectsById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query ("DELETE from Subjects s")
    void deleteAll();

    @Query("SELECT subject_name FROM Subjects")
    List<String> getSubjectsNames();

    @Query(value = "SELECT s.* FROM subjects s, students_subjects ss " +
            "WHERE s.id=ss.subject_id AND ss.student_id=:id", nativeQuery = true)
    List<Subjects> findSubjectsByStudentId(@Param("id") Integer id);

    @Query(value = "SELECT s.* FROM subjects s, instructors_subjects inssub " +
            "WHERE s.id=inssub.subject_id AND inssub.instructor_id=:id", nativeQuery = true)
    List<Subjects> findSubjectsByInstructorId(@Param("id") Integer id);

    @Query(value = "SELECT s FROM subjects s WHERE s.subject_name =: subject_name", nativeQuery = true)
    Subjects findSubjectsByName(String subject_name);
}

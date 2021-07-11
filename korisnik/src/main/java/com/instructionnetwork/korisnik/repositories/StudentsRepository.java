package com.instructionnetwork.korisnik.repositories;

import com.instructionnetwork.korisnik.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentsRepository extends JpaRepository<Students, Integer> {

    @Query("SELECT s FROM Students s")
    List<Students> findAll();

    @Query ("SELECT s.username from Students s")
    List<String> getAllUsernames();

    Students findStudentsById(Integer id);

    Students findStudentsByUsername(String username);

    @Transactional
    @Modifying
    @Query ("DELETE from Students s WHERE s.id=:id")
    void removeStudentsById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query ("DELETE from Students s")
    void deleteAll();
}


package com.instructionnetwork.termini.repositories;

import com.instructionnetwork.termini.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Appointment findByDate(String date);


    List<Appointment> findAll();

    @Transactional
    @Modifying
    void deleteAll();

    Appointment findAppointmentById(Integer id);

    @Transactional
    @Modifying
    void removeAppointmentById(@Param("id") Integer id);

    @Transactional
    @Query(value = "SELECT a.* FROM appointments a WHERE a.instructor_id = :instructor_id", nativeQuery = true)
    List<Appointment> getAppointmentsByInstructor(@Param("instructor_id") Integer instructor_id);

}
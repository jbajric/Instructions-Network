package com.instructionnetwork.rezervacije.repositories;

import com.instructionnetwork.rezervacije.model.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationsRepository extends JpaRepository<Reservations, Integer> {

    List<Reservations> findAll();

    @Transactional
    @Modifying
    void deleteAll();

    Reservations findReservationById(Integer id);
    @Query(value = "SELECT DISTINCT i.* FROM reservations i WHERE i.student_id =:id", nativeQuery = true)
    List<Reservations> getReservationsByStudentId(@Param("id") Integer id);
}

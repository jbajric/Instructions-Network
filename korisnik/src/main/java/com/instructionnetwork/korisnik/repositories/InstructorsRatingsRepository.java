package com.instructionnetwork.korisnik.repositories;

import com.instructionnetwork.korisnik.model.InstructorsRatings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
@Repository
public interface InstructorsRatingsRepository extends JpaRepository<InstructorsRatings, Integer> {

    @Query("SELECT ir FROM InstructorsRatings ir WHERE ir.instructors.id=:instructor_id")
    ArrayList<InstructorsRatings> findRatingsByInstructor(@Param("instructor_id") Integer instructor_id);

    @Query("SELECT ir FROM InstructorsRatings ir")
    ArrayList<InstructorsRatings> findAll();

    @Transactional
    @Modifying
    @Query ("DELETE FROM InstructorsRatings i WHERE i.id=:id")
    void removeRatingById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query ("DELETE from InstructorsRatings i")
    void deleteAll();

    @Query("SELECT AVG(ir.rating) FROM InstructorsRatings ir WHERE ir.instructors.id=?1")
    Double averageRatingForInstructor(Integer id);

}
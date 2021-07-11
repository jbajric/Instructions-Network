package com.instructionnetwork.raspored.repositories;

import com.instructionnetwork.raspored.model.FreeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface FreeTimeRepository extends JpaRepository<FreeTime, Integer> {

    List<FreeTime> findAll();
    FreeTime findFreeTimeById(@Param("id") Integer id);

    @Transactional
    @Modifying
    void removeFreeTimeById(@Param("id") Integer id);

    @Transactional
    @Modifying
    void deleteAll();

    @Transactional
    @Query(value = "SELECT f.* FROM freetimes f WHERE f.student_id = :student_id", nativeQuery = true)
    List<FreeTime> getFreeTimesByStudent(@Param("student_id") Integer student_id);
}

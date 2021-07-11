package com.instructionnetwork.korisnik.repositories;

import com.instructionnetwork.korisnik.model.Instructors;
import com.instructionnetwork.korisnik.projections.InstructorsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorsRepository extends JpaRepository<Instructors, Integer> {

    @Query("SELECT i FROM Instructors i WHERE i.first_name=:firstName AND i.last_name=:lastName")
    Instructors findInstructorsByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Override
    Optional<Instructors> findById(Integer integer);

    @Query ("SELECT i from Instructors i")
    List<Instructors> findAll();

    @Query ("SELECT i.username from Instructors i")
    List<String> getAllUsernames();

    Instructors findInstructorsByUsername(String username);

    Instructors findInstructorsById(Integer id);

    @Transactional
    @Modifying
    @Query ("DELETE FROM Instructors i WHERE i.id=:id")
    void removeInstructorsById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query ("DELETE from Instructors i")
    void deleteAll();

    @Query(value = "SELECT DISTINCT i.* " +
            "FROM instructors i, instructors_subjects inssub, subjects s, students_subjects ss " +
            "WHERE i.id = inssub.instructor_id " +
            "AND inssub.subject_id = s.id " +
            "AND s.id = ss.subject_id " +
            "AND ss.student_id=:id", nativeQuery = true)
    List<Instructors> getInstructorsByStudentsSubjects(@Param("id") Integer id);

    @Query ("SELECT i.id as id, i.first_name as first_name, i.last_name as last_name, i.email as email, i.username as username, AVG(ir.rating) as averageRating " +
            "FROM Instructors i JOIN i.instructorsRatings ir " +
            "GROUP BY i.id")
    List<InstructorsProjection> findAllInstructorsWithAverageRating();
}


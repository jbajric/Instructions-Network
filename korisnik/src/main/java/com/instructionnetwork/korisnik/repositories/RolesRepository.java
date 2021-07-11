package com.instructionnetwork.korisnik.repositories;

import com.instructionnetwork.korisnik.model.Instructors;
import com.instructionnetwork.korisnik.model.Role;
import com.instructionnetwork.korisnik.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleName roleName);

    @Query(value = "SELECT rs.student_id FROM students_roles rs", nativeQuery = true)
    List<Integer> getStudentsRoles();

    @Query(value = "SELECT rs.instructor_id FROM instructors_roles rs", nativeQuery = true)
    List<Integer> getInstructorsRoles();

}

package com.instructionnetwork.systemevents.repository;

import com.instructionnetwork.systemevents.model.SystemEventsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemEventsRepository extends JpaRepository<SystemEventsModel, Integer> {

}
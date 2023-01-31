package com.spring.bioMedical.repository;

import com.spring.bioMedical.entity.Event;
import com.spring.bioMedical.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    long countByEvent(Event event);

    List<Participant> findAllByEvent(Event event);
}
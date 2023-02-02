package com.spring.eventmanagement.repository;

import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    long countByEvent(Event event);

    List<Participant> findAllByEvent(Event event);

    Long countByEmailAndEvent_Id(String email, Integer eventId);
}
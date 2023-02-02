package com.spring.eventmanagement.repository;

import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.entity.Participant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    long countByEvent(Event event);

    List<Participant> findAllByEvent(Event event);

    Long countByEmailAndEvent_Id(String email, Integer eventId);

    Long countByEvent_Id(Integer eventId);

    List<Participant> findAllByEvent_Id(Integer eventId, Pageable pageable);
    List<Participant> findAllByEvent_IdOrderByIdDesc(Integer eventId, Pageable pageable);
}
package com.spring.eventmanagement.service;

import com.spring.eventmanagement.entity.Admin;
import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    public List<Event> findAllByCreatedBy() {
        return eventRepository.findAll();
    }


    public List<Event> findAllByCreatedBy(Admin admin) {
        return eventRepository.findAllByCreatedBy(admin);
    }

    public List<Event> findAllByCreatedByAndIsDeletedFalse(Admin admin ) {
        return eventRepository.findAllByCreatedByAndIsDeletedFalse(admin);
    }


    public Event findById(Integer eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            return eventOptional.get();
        } else {
            return new Event();
        }
    }


    public Event save(Event event) {
        Event e = eventRepository.save(event);
        return e;
    }


}

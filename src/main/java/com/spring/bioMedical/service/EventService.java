package com.spring.bioMedical.service;

import com.spring.bioMedical.entity.Event;
import com.spring.bioMedical.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    public List<Event> findAll() {
        return eventRepository.findAll();
    }


    public Event save(Event event) {
        Event e = eventRepository.save(event);
        return e;
    }


}

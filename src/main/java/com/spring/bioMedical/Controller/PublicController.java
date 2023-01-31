package com.spring.bioMedical.Controller;

import com.spring.bioMedical.entity.Event;
import com.spring.bioMedical.entity.Participant;
import com.spring.bioMedical.repository.ParticipantRepository;
import com.spring.bioMedical.service.EventService;
import com.spring.bioMedical.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Slf4j
@RequestMapping("/public")
public class PublicController extends Utils {


    private final EventService eventService;
    private final ParticipantRepository participantRepository;

    public PublicController(EventService eventService, ParticipantRepository participantRepository) {
        this.eventService = eventService;
        this.participantRepository = participantRepository;
    }


    @PostMapping("/participant")
    public String addParticipant(@ModelAttribute("participant") Participant participant) {
        Event event = eventService.findById(participant.getEvent().getId());
        participant.setEvent(event);
        participant.setCreation(new Date());


        participantRepository.save(participant);


        long count = participantRepository.countByEvent(event);
        event.setGoing((int)count);
        eventService.save(event);

        return "redirect:/public/" + event.getId();
    }


    @GetMapping("/{id}")
    public String eventDetails(@PathVariable("id") Integer id, Model theModel) {

        Event event = eventService.findById(id);
        Participant participant = new Participant();
        participant.setEvent(event);


        theModel.addAttribute("event", event);
        theModel.addAttribute("participant", participant);
        theModel.addAttribute("eventtime", Utils.getEventTime(event.getStart(), event.getEnd()));

        return "admin/eventDetails";
    }


}

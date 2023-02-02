package com.spring.eventmanagement.Controller;

import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.entity.Participant;
import com.spring.eventmanagement.repository.ParticipantRepository;
import com.spring.eventmanagement.service.EventService;
import com.spring.eventmanagement.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        Long cnt = participantRepository.countByEmailAndEvent_Id(participant.getEmail(), participant.getEvent().getId());
        if (cnt > 0L) {
            return "redirect:/public/" + participant.getEvent().getId() + "?msg=exist";
        }
        Event event = eventService.findById(participant.getEvent().getId());
        participant.setEvent(event);
        participant.setCreation(new Date());


        participantRepository.save(participant);


        long count = participantRepository.countByEvent(event);
        event.setGoing((int) count);
        eventService.save(event);

//        return eventDetails(event.getId(), theModel);
        return "redirect:/public/" + event.getId() + "?msg=success";
    }


    @GetMapping("/{id}")
    public String eventDetails(@PathVariable("id") Integer id, Model theModel,
                               @RequestParam(value = "msg", required = false) String msg) {


        Event event = eventService.findById(id);
        Participant participant = new Participant();
        participant.setEvent(event);
        if (msg != null) {

            if (msg.equals("success"))
                theModel.addAttribute("confirmationMessage", "You are Registered For this Event.");
            if (msg.equals("exist"))
                theModel.addAttribute("confirmationMessageDanger", "You are Already Registered For this Event.");

        }

        theModel.addAttribute("event", event);
        theModel.addAttribute("participant", participant);
        theModel.addAttribute("eventtime", Utils.getEventTime(event.getStart(), event.getEnd()));

        return "admin/eventDetails";
    }


}

package com.spring.bioMedical.Controller;

import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.entity.Event;
import com.spring.bioMedical.repository.AdminRepository;
import com.spring.bioMedical.repository.EventRepository;
import com.spring.bioMedical.service.AdminServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
class EventController {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AdminServiceImplementation adminService;
    @Autowired
    private AdminRepository adminRepository;


    @RequestMapping(value = "/allevents", method = RequestMethod.GET)
    public List<Event> allEvents() {
        return eventRepository.findAll();
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    public Event addEvent(@RequestBody Event event) {
        event.setCreation(new Date());
        Admin admin = adminRepository.findByEmail("raju");
        event.setCreatedBy(admin);
//        event.setCreatedBy(getLoginUser());

        Event created = eventRepository.save(event);
        return created;
    }

    @RequestMapping(value = "/event", method = RequestMethod.PATCH)
    public Event updateEvent(@RequestBody Event event) {
        Optional<Event> optionalEvent = eventRepository.findById(event.getId());
        if (optionalEvent.isPresent()) {
            Event old = optionalEvent.get();
            old.setTitle(event.getTitle());
            old.setDescription(event.getDescription());
            old.setStart(event.getStart());
            old.setEnd(event.getEnd());
            old.setModification(new Date());

            return eventRepository.save(old);
        }
        return event;
    }

    @RequestMapping(value = "/event", method = RequestMethod.DELETE)
    public void removeEvent(@RequestBody Event event) {
        eventRepository.delete(event);
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public List<Event> getEventsInRange(@RequestParam(value = "start", required = true) String start,
                                        @RequestParam(value = "end", required = true) String end) {
        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            startDate = inputDateFormat.parse(start);
        } catch (ParseException e) {
            throw new BadDateFormatException("bad start date: " + start);
        }

        try {
            endDate = inputDateFormat.parse(end);
        } catch (ParseException e) {
            throw new BadDateFormatException("bad end date: " + end);
        }

//        LocalDateTime startDateTime = LocalDateTime.ofInstant(startDate.toInstant(),
//                ZoneId.systemDefault());
//
//        LocalDateTime endDateTime = LocalDateTime.ofInstant(endDate.toInstant(),
//                ZoneId.systemDefault());

        return eventRepository.findByDateBetween(startDate, endDate);
    }

    public Admin getLoginUser() {
        // get last seen
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            String Pass = ((UserDetails) principal).getPassword();
            System.out.println("One + " + username + "   " + Pass);


        } else {
            username = principal.toString();
            System.out.println("Two + " + username);
        }

        return adminService.findByEmail(username);
    }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadDateFormatException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadDateFormatException(String dateString) {
        super(dateString);
    }
}
package com.spring.eventmanagement.Controller;

import com.spring.eventmanagement.entity.Admin;
import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.repository.AdminRepository;
import com.spring.eventmanagement.repository.EventRepository;
import com.spring.eventmanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;


    @RequestMapping(value = "/allevents", method = RequestMethod.GET)
    public List<Event> allEvents() {
        return eventRepository.findAll();
    }


    @RequestMapping(value = "/allevents/{adminId}", method = RequestMethod.GET)
    public List<Event> allEventsAdmin(@PathVariable("adminId") Integer adminId) {
        return eventRepository.findAllByCreatedBy(Admin.builder().id(adminId).build());
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    public Event addEvent(@RequestBody Event event) {
        event.setCreation(new Date());
        Admin admin = adminService.findById(event.getCreatedBy().getId());
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
            old.setLocation(event.getLocation());
            old.setStart(event.getStart());
            old.setEnd(event.getEnd());
            old.setModification(new Date());

            return eventRepository.save(old);
        }
        return event;
    }

    @RequestMapping(value = "/event", method = RequestMethod.DELETE)
    public Event removeEvent(@RequestBody Event event) {
        eventRepository.delete(event);
        return event;
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


        return eventRepository.findByDateBetween(startDate, endDate);
    }

    @RequestMapping(value = "/events/{adminId}", method = RequestMethod.GET)
    public List<Event> getEventsInRangeAdmin(
            @PathVariable("adminId") Integer adminId,
            @RequestParam(value = "start", required = true) String start,
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


        return eventRepository.findByDateBetween(startDate, endDate, adminId);
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

    @RequestMapping(value = "/events-dt/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Wrapper> listAllProducts(
            @PathVariable("adminId") Integer adminId,
            @RequestParam("start") int start,
            @RequestParam("draw") int draw,
            @RequestParam("length") int pageLength
    ) {
        Long count = eventRepository.countByCreatedBy(Admin.builder().id(adminId).build());
        List<Event> events = eventRepository.findAllByCreatedBy(Admin.builder().id(adminId).build(), PageRequest.of(0, 2));
        Wrapper w = new Wrapper(events, count.intValue(), start, events.size(),draw);

        return new ResponseEntity<>(w, HttpStatus.OK);
    }


}


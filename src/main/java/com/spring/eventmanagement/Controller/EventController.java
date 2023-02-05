package com.spring.eventmanagement.Controller;

import com.spring.eventmanagement.entity.Admin;
import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.repository.AdminRepository;
import com.spring.eventmanagement.repositoryDatatable.EventDatatableRepository;
import com.spring.eventmanagement.repository.EventRepository;
import com.spring.eventmanagement.repositoryDatatable.SearchCriteria;
import com.spring.eventmanagement.repositoryDatatable.EventSpecification;
import com.spring.eventmanagement.service.AdminService;
import com.spring.eventmanagement.utils.SearchParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private EventDatatableRepository eventDatatableRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;


    @RequestMapping(value = "/allevents", method = RequestMethod.GET)
    public List<Event> allEvents() {
        return eventRepository.findAllByIsDeletedFalse();
    }


    @RequestMapping(value = "/allevents/{adminId}", method = RequestMethod.GET)
    public List<Event> allEventsAdmin(
            @PathVariable("adminId") Integer adminId
//            @RequestParam("start") Date start,
//            @RequestParam("end") Date end
    ) {
        List<Event> events = eventRepository.findAllByCreatedByAndIsDeletedFalse(
                Admin.builder().id(adminId).build()
        );
//        List<Event> events = eventRepository.findAllByCreatedByAndIsDeletedFalseAndStartAndEnd(
//                Admin.builder().id(adminId).build(),
//                start,
//                end
//        );

        return events;
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
        Optional<Event> eventOptional = eventRepository.findById(event.getId());
        if (eventOptional.isPresent()) {
            event = eventOptional.get();
            event.setDeleted(true);
            eventRepository.save(event);
//            return new ResponseEntity<>(event, HttpStatus.OK);

        }
//        eventRepository.delete(event);
        return event;
    }

    @RequestMapping(value = "/event/{eventId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Event> removeEvent(@PathVariable("eventId") Integer eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            event.setDeleted(true);
            eventRepository.save(event);
            return new ResponseEntity<>(event, HttpStatus.OK);

        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

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

    @RequestMapping(value = "/events-dt1/{adminId}", method = RequestMethod.GET)

    public Wrapper listAllProducts(
            @PathVariable("adminId") Integer adminId,
            @RequestParam("start") int start,
            @RequestParam("draw") int draw,
            @RequestParam("length") int pageLength,
            @RequestParam("search") SearchParameter SearchParameter
    ) {
        Long count = eventRepository.countByCreatedByAndIsDeletedFalse(Admin.builder().id(adminId).build());
        List<Event> events = eventRepository.findAllByCreatedByAndIsDeletedFalseOrderByIdDesc(Admin.builder().id(adminId).build(), PageRequest.of(start, pageLength));
        Wrapper w = new Wrapper(events, count.intValue(), start, events.size(), draw);

        return w;
    }

    @RequestMapping(value = "/events-dt/{adminId}", method = RequestMethod.GET)

    public DataTablesOutput<Event> listAllProducts(
            @PathVariable("adminId") Integer adminId,
            @Valid DataTablesInput dataTablesInput
    ) {

        EventSpecification spec =
                new EventSpecification(
                        new SearchCriteria("createdBy",
                                ":",
                              Admin.builder().id(  adminId).build()
                        )
                );
        EventSpecification spec2 =
                new EventSpecification(
                        new SearchCriteria("isDeleted",
                                ":",
                              false
                        )
                );

        return eventDatatableRepository.findAll(
                 dataTablesInput
                ,spec.and(spec2)
        );
//        return eventDatatableRepository.findAllByCreatedByAndIsDeletedFalseOrderByIdDesc(
//                Admin.builder().id(adminId).build()
//                , dataTablesInput
//        );


    }


}


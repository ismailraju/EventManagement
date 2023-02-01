package com.spring.eventmanagement.Controller;

import com.spring.eventmanagement.entity.Admin;
import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.entity.Participant;
import com.spring.eventmanagement.repository.ParticipantRepository;
import com.spring.eventmanagement.service.AdminService;
import com.spring.eventmanagement.service.EventService;
import com.spring.eventmanagement.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {


    private final EventService eventService;

    private final AdminService adminService;


    private final ParticipantRepository participantRepository;


    @Autowired
    public AdminController(EventService eventService, AdminService obj,
                           ParticipantRepository participantRepository) {
        this.eventService = eventService;
        adminService = obj;

        this.participantRepository = participantRepository;
    }



    @GetMapping("/profile")
    public String EditForm(Model theModel) {

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

        // get the employee from the service

        Admin admin = adminService.findByEmail(username);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();

        adminService.save(admin);

        System.out.println(admin);

        theModel.addAttribute("profile", admin);

//        return "admin/updateMyProfile";
        return "admin/updateProfile";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute("profile") Admin admin, Model model) {


        System.out.println(admin);
        Admin adminOld = adminService.findById(admin.getId());

        adminOld.setEmail(admin.getEmail());
        adminService.save(adminOld);

        model.addAttribute("confirmationMessage",
                "e-mail address updated." + admin.getEmail());

        return "redirect:/admin/profile";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@ModelAttribute("profile") Admin admin) {

        System.out.println(admin);
        Admin adminOld = adminService.findById(admin.getId());

        adminOld.setPassword(admin.getPassword());
        adminService.save(adminOld);


        return "redirect:/admin/profile";
    }


    @GetMapping("/create-event")
    public String EventForm(Model theModel) {


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

        Admin admin = adminService.findByEmail(username);


        Event event = new Event();
        theModel.addAttribute("event", event);
        theModel.addAttribute("adminId", admin.getId());
        return "admin/eventCreate";
    }

    @PostMapping("/create-event")
    public String saveEvent(@ModelAttribute("event") @Valid Event event, Model theModel, Errors errors) {

        if (null != errors && errors.getErrorCount() > 0) {
            theModel.addAttribute("event", event);
            return "admin/eventCreate";
        }
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

        Admin admin = adminService.findByEmail(username);

        System.out.println(admin);
        event.setCreatedBy(admin);

        eventService.save(event);

        // use a redirect to prevent duplicate submissions
        return "redirect:/admin/profile";
    }


    @RequestMapping("/events")
    public String events(Model model) {

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

        Admin admin = adminService.findByEmail(username);

        List<Event> events = eventService.findAllByCreatedBy(admin);
        model.addAttribute("events", events);


        return "admin/events";
    }


    @GetMapping("/event/{id}")
    public String eventDetails(@PathVariable("id") Integer id, Model theModel) {

        Event event = eventService.findById(id);


        List<Participant> participants = participantRepository.findAllByEvent(event);
        theModel.addAttribute("event", event);
        theModel.addAttribute("participants", participants);

        theModel.addAttribute("eventtime", Utils.getEventTime(event.getStart(), event.getEnd()));

        return "admin/eventAdmin";
    }


}

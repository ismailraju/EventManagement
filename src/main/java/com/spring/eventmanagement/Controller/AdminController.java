package com.spring.eventmanagement.Controller;

import com.spring.eventmanagement.entity.Admin;
import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.entity.Participant;
import com.spring.eventmanagement.repository.ParticipantRepository;
import com.spring.eventmanagement.service.AdminService;
import com.spring.eventmanagement.service.EventService;
import com.spring.eventmanagement.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        } else {
            username = principal.toString();
        }


        Admin admin = adminService.findByEmail(username);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();

        adminService.save(admin);

        System.out.println(admin);

        theModel.addAttribute("profile", admin);

        return "admin/updateProfile";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute("profile") Admin admin, Model model, HttpServletRequest request) {


        Long userExists = adminService.countByEmailAndIdNot(admin.getEmail(), admin.getId());

        System.out.println(userExists);

        if (userExists > 0L) {
            model.addAttribute("confirmationMessageEmail", "Oops!  There is already a user registered with the email provided.");
            return EditForm(model);
        }


        System.out.println(admin);
        Admin adminOld = adminService.findById(admin.getId());

        adminOld.setEmail(admin.getEmail());
        adminService.save(adminOld);

        model.addAttribute("confirmationMessageEmail",
                "e-mail address updated." + admin.getEmail());
        sessionInvalidate(request);
        return "redirect:/?msg=em";
//        return EditForm(model);
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@ModelAttribute("profile") Admin admin, Model model,HttpServletRequest request) {


        if (!admin.getPassword().equals(admin.getPassword2())) {
            model.addAttribute("confirmationMessagePassword", "Oops!  Password Not match.");
            return EditForm(model);
        }


        Admin adminOld = adminService.findById(admin.getId());

        adminOld.setPassword(admin.getPassword());
        adminService.save(adminOld);

        model.addAttribute("confirmationMessagePassword",
                "Password updated.");

        sessionInvalidate(request);
//        return "redirect:/admin/profile";
//        return EditForm(model);
        return "redirect:/?msg=ps";
    }
void sessionInvalidate(HttpServletRequest request){
    HttpSession session= request.getSession(false);
    SecurityContextHolder.clearContext();
    if(session != null) {
        session.invalidate();
    }
}

    @GetMapping("/create-event")
    public String EventForm(Model theModel) {

        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            String Pass = ((UserDetails) principal).getPassword();


        } else {
            username = principal.toString();
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

        List<Event> events = eventService.findAllByCreatedByAndIsDeletedFalse(admin);
        model.addAttribute("events", events);
        model.addAttribute("adminId", admin.getId());


        return "admin/events";
    }


    @GetMapping("/event/{id}")
    public String eventDetails(@PathVariable("id") Integer id, Model theModel) {

        Event event = eventService.findById(id);


        List<Participant> participants = participantRepository.findAllByEvent(event);
        theModel.addAttribute("event", event);
        theModel.addAttribute("participants", participants);
        theModel.addAttribute("eventId", event.getId());

        theModel.addAttribute("eventtime", Utils.getEventTime(event.getStart(), event.getEnd()));

        return "admin/eventAdmin";
    }


    @RequestMapping(value = "/participants/{eventId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Wrapper> listAllProducts(
            @PathVariable("eventId") Integer eventId,
            @RequestParam("start") int start,
            @RequestParam("draw") int draw,
            @RequestParam("length") int pageLength
    ) {
        Long count = participantRepository.countByEvent_Id(eventId);
        List<Participant> participants = participantRepository.findAllByEvent_IdOrderByIdDesc(eventId, PageRequest.of(start, pageLength));

        Wrapper w = new Wrapper(participants, count.intValue(), pageLength, participants.size(),draw);

        return new ResponseEntity<>(w, HttpStatus.OK);
    }


}

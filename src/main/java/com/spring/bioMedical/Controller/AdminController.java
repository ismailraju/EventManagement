package com.spring.bioMedical.Controller;

import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.entity.Appointment;
import com.spring.bioMedical.entity.Event;
import com.spring.bioMedical.service.AdminServiceImplementation;
import com.spring.bioMedical.service.AppointmentServiceImplementation;
import com.spring.bioMedical.service.EventService;
import com.spring.bioMedical.service.UserService;
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
    private final UserService userService;

    private final AdminServiceImplementation adminServiceImplementation;

    private final AppointmentServiceImplementation appointmentServiceImplementation;


    @Autowired
    public AdminController(EventService eventService, UserService userService, AdminServiceImplementation obj,
                           AppointmentServiceImplementation app) {
        this.eventService = eventService;
        this.userService = userService;
        adminServiceImplementation = obj;
        appointmentServiceImplementation = app;
    }


    @RequestMapping("/user-details")
    public String index(Model model) {


        List<Admin> list = adminServiceImplementation.findByRole("ROLE_USER");
        model.addAttribute("user", list);


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

        Admin admin = adminServiceImplementation.findByEmail(username);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();

        String log = now.toString();

        admin.setLastseen(log);

        adminServiceImplementation.save(admin);


        return "admin/user";
    }

    @RequestMapping("/doctor-details")
    public String doctorDetails(Model model) {


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

        Admin admin = adminServiceImplementation.findByEmail(username);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();

        String log = now.toString();

        admin.setLastseen(log);

        adminServiceImplementation.save(admin);


        List<Admin> list = adminServiceImplementation.findByRole("ROLE_DOCTOR");


        // add to the spring model
        model.addAttribute("user", list);


        return "admin/doctor";
    }

    @RequestMapping("/admin-details")
    public String adminDetails(Model model) {


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

        Admin admin = adminServiceImplementation.findByEmail(username);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();

        String log = now.toString();

        admin.setLastseen(log);

        adminServiceImplementation.save(admin);


        List<Admin> list = adminServiceImplementation.findByRole("ROLE_ADMIN");


        // add to the spring model
        model.addAttribute("user", list);


        return "admin/admin";
    }


    @GetMapping("/add-doctor")
    public String showFormForAdd(Model theModel) {


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

        Admin admin1 = adminServiceImplementation.findByEmail(username);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();

        String log = now.toString();

        admin1.setLastseen(log);

        adminServiceImplementation.save(admin1);


        // create model attribute to bind form data
        Admin admin = new Admin();

        theModel.addAttribute("doctor", admin);

        return "admin/addDoctor";
    }


    @PostMapping("/save-doctor")
    public String saveEmployee(@ModelAttribute("doctor") Admin admin) {

        // save the employee
        //	admin.setId(0);

        admin.setRole("ROLE_DOCTOR");

        admin.setPassword("default");

        admin.setEnabled(true);

        admin.setConfirmationToken("ByAdmin-Panel");

        System.out.println(admin);

        adminServiceImplementation.save(admin);

        // use a redirect to prevent duplicate submissions
        return "redirect:/admin/userdetails";
    }


    @GetMapping("/add-admin")
    public String showForm(Model theModel) {


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

        Admin admin1 = adminServiceImplementation.findByEmail(username);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();

        String log = now.toString();

        admin1.setLastseen(log);

        adminServiceImplementation.save(admin1);


        // create model attribute to bind form data
        Admin admin = new Admin();

        theModel.addAttribute("doctor", admin);

        return "admin/addAdmin";
    }


    @PostMapping("/save-admin")
    public String saveEmploye(@ModelAttribute("doctor") Admin admin) {

        // save the employee
        //	admin.setId(0);

        admin.setRole("ROLE_ADMIN");

        admin.setPassword("default");

        admin.setEnabled(true);

        admin.setConfirmationToken("ByAdmin-Panel");

        System.out.println(admin);

        adminServiceImplementation.save(admin);

        // use a redirect to prevent duplicate submissions
        return "redirect:/admin/userdetails";
    }

    @GetMapping("/edit-my-profile")
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

        Admin admin = adminServiceImplementation.findByEmail(username);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();

        String log = now.toString();

        admin.setLastseen(log);

        adminServiceImplementation.save(admin);

        System.out.println(admin);

        theModel.addAttribute("profile", admin);

        return "admin/updateMyProfile";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute("profile") Admin admin) {


        System.out.println(admin);

        adminServiceImplementation.save(admin);

        // use a redirect to prevent duplicate submissions
        return "redirect:/admin/user-details";
    }


    @RequestMapping("/appointments")
    public String appointments(Model model) {


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

        Admin admin = adminServiceImplementation.findByEmail(username);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();

        String log = now.toString();

        admin.setLastseen(log);

        adminServiceImplementation.save(admin);


        List<Appointment> list = appointmentServiceImplementation.findAll();


        // add to the spring model
        model.addAttribute("app", list);


        return "admin/appointment";
    }


    @GetMapping("/create-event")
    public String EventForm(Model theModel) {
        Event event = new Event();
        theModel.addAttribute("event", event);
        return "admin/createEvent";
    }

    @PostMapping("/create-event")
    public String saveEvent(@ModelAttribute("event") @Valid Event event, Model theModel, Errors errors) {

        if (null != errors && errors.getErrorCount() > 0) {
            theModel.addAttribute("event", event);
            return "admin/createEvent";
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

        Admin admin = adminServiceImplementation.findByEmail(username);

        System.out.println(admin);
        event.setCreatedBy(admin);

        eventService.save(event);

        // use a redirect to prevent duplicate submissions
        return "redirect:/admin/user-details";
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

        Admin admin = adminServiceImplementation.findByEmail(username);

        List<Event> events = eventService.findAllByCreatedBy(admin);
        model.addAttribute("events", events);


        return "admin/events";
    }


    @GetMapping("/event/{id}")
    public String eventDetails(@RequestParam("id") Integer id, Model theModel) {

        Event event = eventService.findById(id);

        theModel.addAttribute("event", event);

        return "admin/eventDetails";
    }


}

package com.spring.bioMedical.Controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class RegisterController {

    //private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AdminService adminService;

    @Autowired
    public RegisterController(
            AdminService adminService) {
        //this.bCryptPasswordEncoder = bCryptPasswordEncoder;

        this.adminService = adminService;
    }

    // Return registration form template
    @RequestMapping(value = "/register", method = RequestMethod.GET)

    public ModelAndView showRegistrationPage(ModelAndView modelAndView, Admin user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    // Process form input data
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid Admin user, BindingResult bindingResult, HttpServletRequest request) {


        if (!user.getPassword().equals(user.getPassword2())) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  Password Not match.");
            modelAndView.setViewName("registration");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        // Lookup user in database by e-mail
        Admin userExists = adminService.findByEmail(user.getEmail());

        System.out.println(userExists);

        if (userExists != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            modelAndView.setViewName("registration");
            modelAndView.addObject("user", user);
            return modelAndView;
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {


            user.setEnabled(true);
            user.setRole("ROLE_ADMIN");


            // Generate random 36-character string token for confirmation link
            user.setConfirmationToken(UUID.randomUUID().toString());

            adminService.save(user);


            modelAndView.addObject("confirmationMessage", "You are registered for this email." + user.getEmail());
            modelAndView.setViewName("registration");
        }
        modelAndView.addObject("user", user);
        return modelAndView;
    }


}
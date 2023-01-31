package com.spring.bioMedical.Controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.spring.bioMedical.service.EmailService;

/**
 * 
 * @author Soumyadip Chowdhury
 * @github soumyadip007
 *
 */
@Controller
public class RegisterController {
	
	//private BCryptPasswordEncoder bCryptPasswordEncoder;
	private EmailService emailService;
	private AdminService adminService;

	@Autowired
	public RegisterController(
			 EmailService emailService) {
		//this.bCryptPasswordEncoder = bCryptPasswordEncoder;

		this.emailService = emailService;
	}
	
	// Return registration form template
	@RequestMapping(value="/register", method = RequestMethod.GET)

	public ModelAndView showRegistrationPage(ModelAndView modelAndView, Admin user){
		modelAndView.addObject("user", user);
//		modelAndView.setViewName("register");
		modelAndView.setViewName("registation");
		return modelAndView;
	}
	
	// Process form input data
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid Admin user, BindingResult bindingResult, HttpServletRequest request) {
				
		// Lookup user in database by e-mail
		Admin userExists = adminService.findByEmail(user.getEmail());
		
		System.out.println(userExists);
		
		if (userExists != null) {
			modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
			modelAndView.setViewName("registation");
			bindingResult.reject("email");
		}
			
		if (bindingResult.hasErrors()) { 
			modelAndView.setViewName("registation");
		} else { // new user so we create user and send confirmation e-mail
					
			// Disable user until they click on confirmation link in email
		    
			user.setEnabled(true);
			user.setRole("ROLE_USER");
		      
			
		    // Generate random 36-character string token for confirmation link
		    user.setConfirmationToken(UUID.randomUUID().toString());

			adminService.save(user);
				
		//	String appUrl = request.getScheme() + "://" + request.getServerName();
			
		    String appUrl = "localhost:8080";
		    
		    
			SimpleMailMessage registrationEmail = new SimpleMailMessage();
			registrationEmail.setTo(user.getEmail());
			registrationEmail.setSubject("Registration Confirmation");
			registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
					+ appUrl + "/confirm?token=" + user.getConfirmationToken());
			registrationEmail.setFrom("spring.email.auth@gmail.com");
			
//			emailService.sendEmail(registrationEmail);
			
			modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());
			modelAndView.setViewName("registation");
		}
			
		return modelAndView;
	}

	
}
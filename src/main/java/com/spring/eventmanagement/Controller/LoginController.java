package com.spring.eventmanagement.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {


    @RequestMapping("/")
    public String showHome(Model model,
                           @RequestParam(value = "msg", required = false) String msg) {

        if (msg != null) {
            if (msg.equals("ps"))
                model.addAttribute("confirmationMessage", "Your Password updated.");
            if (msg.equals("em"))
                model.addAttribute("confirmationMessage", "Your Email updated.");
        }

        return "login";
    }


}

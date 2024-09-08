package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.repsitories.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    // verify email

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/verify-email")
    public String verifyEmail(
            @RequestParam("token") String token, HttpSession session) {

        User user = userRepo.findByEmailToken(token).orElse(null);

        if (user != null) {
            // user fetch hua hai :: process karna hai

            if (user.getEmailToken().equals(token)) {
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);
                
                Message message=new Message();
                message.setContent("You email is verified. Now you can login ");
                message.setType(MessageType.green);

                session.setAttribute("message",message);

            }
            
            Message message1=new Message();
            message1.setContent("Email not verified ! Token is not associated with user .");
            message1.setType(MessageType.red);

            session.setAttribute("message",message1);
            return "error_page";

        }
        
        Message message2=new Message();
        message2.setContent("Email not verified ! Token is not associated with user .");
        message2.setType(MessageType.red);

        session.setAttribute("message",message2);

    
        return "error_page";
    }

}

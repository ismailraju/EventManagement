package com.spring.eventmanagement.service;


import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.entity.Participant;
import com.spring.eventmanagement.payload.EmailDetails;
import com.spring.eventmanagement.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// Annotation
@Service
// Class
// Implementing EmailService interface
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ParticipantRepository participantRepository;

    @Value("${spring.mail.username}")
    private String sender;

    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MMM/dd HH:mm");

    // Method 1
    // To send a simple email
    public String sendSimpleMail(EmailDetails details) {

        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }
    }

    // Method 2
    // To send an email with attachment
    public String sendMailWithAttachment(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                    details.getSubject());

            // Adding the attachment
            FileSystemResource file
                    = new FileSystemResource(
                    new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(
                    file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }

    public void sendNewEventForOrganizer(Event event) {
        String subject = event.getTitle() + " Event Created";
        String body = "You have Created a Event (" + event.getTitle() + ")." +
                getEventDetails(event);


        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(event.getCreatedBy().getEmail())
                .subject(subject)
                .msgBody(body)
                .build();

        sendSimpleMail(emailDetails);

    }


    public void sendUpdateEventForOrganizer(Event event) {
        String subject = event.getTitle() + " Event Updated";
        String body = "You have Updated a Event (" + event.getTitle() + ")." +
                getEventDetails(event);


        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(event.getCreatedBy().getEmail())
                .subject(subject)
                .msgBody(body)
                .build();

        sendSimpleMail(emailDetails);

    }

    public void sendUpdateEventForParticipant(Event event) {
        String subject = event.getTitle() + " Event Updated";
        String body = "The Event (" + event.getTitle() + ")  updated." +
                getEventDetails(event);


        List<Participant> participants = participantRepository.findAllByEvent(event);

        for (int i = 0; i < participants.size(); i++) {

            Participant pp = participants.get(i);
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(pp.getEmail())
                    .subject(subject)
                    .msgBody(body)
                    .build();

            sendSimpleMail(emailDetails);


        }

    }

    public void sendNewParticipant(Participant participant) {
        String subject = "Registration of " + participant.getEvent().getTitle();
        String body = "You have Register  for  Event '" + participant.getEvent().getTitle() + "'." +
                getEventDetails(participant.getEvent());


        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(participant.getEmail())
                .subject(subject)
                .msgBody(body)
                .build();

        sendSimpleMail(emailDetails);

    }

    public void sendOrganizerAboutNewParticipant(Participant participant) {
        String subject = "New Participant of " + participant.getEvent().getTitle();
        String body = "You have got a new Participant (" + participant.getEmail() + ") for  Event '" + participant.getEvent().getTitle() + "'." +
                getEventDetails(participant.getEvent());


        List<Participant> participants = participantRepository.findAllByEvent(participant.getEvent());
        AtomicInteger cnt = new AtomicInteger(1);
        String participantListStr = participants.stream().map(e -> (cnt.getAndIncrement()) + ". " + e.getName() + "(" + e.getEmail() + ")").collect(Collectors.joining("\n"));
        body += "\nAll Participants: \n" + participantListStr;


        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(participant.getEvent().getCreatedBy().getEmail())
                .subject(subject)
                .msgBody(body)
                .build();

        sendSimpleMail(emailDetails);

    }

    public void sendFriendParticipant(Participant participant) {
        String subject = "Participants for " + participant.getEvent().getTitle();
        String body = "Participants  for  Event '" + participant.getEvent().getTitle() + "'." +
                getEventDetails(participant.getEvent());

        List<Participant> participants = participantRepository.findAllByEvent(participant.getEvent());
        AtomicInteger cnt = new AtomicInteger(1);
        String participantListStr = participants.stream().map(e -> (cnt.getAndIncrement()) + ". " + e.getName() + "(" + e.getEmail() + ")").collect(Collectors.joining("\n"));
        body += "\nParticipants: \n" + participantListStr;
        for (int i = 0; i < participants.size() && participants.size() > 1; i++) {

            Participant pp = participants.get(i);
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(pp.getEmail())
                    .subject(subject)
                    .msgBody(body)
                    .build();

            sendSimpleMail(emailDetails);


        }

    }

    String getEventDetails(Event event) {
        return "\nDetails: " + event.getDescription() +
                "\nLocation: " + event.getLocation() + "" +
                "\nTime: " + dateFormat.format(event.getStart()) + " to " + dateFormat.format(event.getEnd());


    }

    @Async
    public void newParticipant(Participant participant) {
        sendOrganizerAboutNewParticipant(participant);
        sendNewParticipant(participant);
//        sendFriendParticipant(participant);
    }

    @Async
    public void newEvent(Event event) {
        sendNewEventForOrganizer(event);

    }

    @Async
    public void updateEvent(Event event) {
        sendUpdateEventForOrganizer(event);
        sendUpdateEventForParticipant(event);

    }

    @Async
    public void before1hour(Event event) {
        sendBefore1hourForOrganizer(event);
        sendBefore1hourForParticipant(event);

    }

    public void sendBefore1hourForOrganizer(Event event) {
        String subject = event.getTitle() + " will be held within 1 hour";
        String body = "The Event (" + event.getTitle() + ")  will be held within 1 hour." +
                getEventDetails(event);

        List<Participant> participants = participantRepository.findAllByEvent(event);
        AtomicInteger cnt = new AtomicInteger(1);
        String participantListStr = participants.stream().map(e -> (cnt.getAndIncrement()) + ". " + e.getName() + "(" + e.getEmail() + ")").collect(Collectors.joining("\n"));
        body += "\nParticipants: \n" + participantListStr;

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(event.getCreatedBy().getEmail())
                .subject(subject)
                .msgBody(body)
                .build();

        sendSimpleMail(emailDetails);

    }

    public void sendBefore1hourForParticipant(Event event) {
        String subject = event.getTitle() + " will be held within 1 hour";
        String body = "The Event (" + event.getTitle() + ")   will be held within 1 hour." +
                getEventDetails(event);


        List<Participant> participants = participantRepository.findAllByEvent(event);

        for (int i = 0; i < participants.size(); i++) {

            Participant pp = participants.get(i);
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(pp.getEmail())
                    .subject(subject)
                    .msgBody(body)
                    .build();

            sendSimpleMail(emailDetails);


        }

    }


}

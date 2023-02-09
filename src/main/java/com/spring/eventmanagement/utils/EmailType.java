package com.spring.eventmanagement.utils;

public enum EmailType {
    NEW_EVENT_ORGANIZER("Create New event", "You create an Event as Organizer"),
    UPDATE_EVENT_ORGANIZER("Update an Event", "body"),
    UPDATE_EVENT_PARTICIPANT("subject", "body"),
    NEW_PARTICIPANT("You are participant", "body"),
    FRIEND_PARTICIPANT("your friend of the Event", "body"),
    BEFORE_1_HOUR_PARTICIPANT("plz Attend as Participant", "body"),
    BEFORE_1_HOUR_ORGANIZER("plz Attend as Organizer", "body");

   private final String subject;
   private final String body;

    EmailType(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}

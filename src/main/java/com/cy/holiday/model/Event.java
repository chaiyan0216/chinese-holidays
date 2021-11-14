package com.cy.holiday.model;

import java.time.LocalDate;

public class Event {

    private final LocalDate date;
    private final String summary;
    private final String description;
    private final String uid;

    public Event(LocalDate date, String summary, String description) {
        this.date = date;
        this.summary = summary;
        this.description = description;
        this.uid = date + "_" + summary.replace(" ", "");
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getUid() {
        return uid;
    }
}

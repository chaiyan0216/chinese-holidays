package com.cy.holiday.model;

import java.time.LocalDate;
import java.util.UUID;

public class Event {

    private final LocalDate date;
    private final String summary;
    private final String uid;

    public Event(LocalDate date, String summary) {
        this.date = date;
        this.summary = summary;
        this.uid = date + "_" + summary.replace(" ", "");
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSummary() {
        return summary;
    }

    public String getUid() {
        return uid;
    }
}

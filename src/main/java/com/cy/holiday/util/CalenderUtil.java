package com.cy.holiday.util;

import com.cy.holiday.model.Event;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;

public class CalenderUtil {

    private CalenderUtil() {
    }

    public static void generateCalender(List<Event> events) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId(PropertyUtil.get("ical.prod.id")));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        calendar.getProperties().add(Method.PUBLISH);
        calendar.getProperties().add(new XProperty("X-WR-CALNAME", PropertyUtil.get("ical.calname")));
        calendar.getProperties().add(new XProperty("X-WR-CALDESC", PropertyUtil.get("ical.caldesc")));
        calendar.getProperties().add(new XProperty("X-WR-TIMEZONE", PropertyUtil.get("ical.timezone")));

        for (Event event : events) {
            java.util.Date date = Date.from(event.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            VEvent vEvent = new VEvent(new Date(date), new Date(date), event.getSummary());
            vEvent.getProperties().add(new Uid(event.getUid()));
            vEvent.getProperties().remove(new DtStamp());
            vEvent.getProperties().add(new DtStamp(new DateTime(date)));
            calendar.getComponents().add(vEvent);
        }

        try {
            FileOutputStream fos = new FileOutputStream(PropertyUtil.get("ics.path"));
            CalendarOutputter calendarOutputter = new CalendarOutputter();
            calendarOutputter.output(calendar, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

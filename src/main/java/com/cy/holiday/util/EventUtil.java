package com.cy.holiday.util;

import com.cy.holiday.model.Event;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EventUtil {

    private static final String HOLIDAY_TEMPLATE = PropertyUtil.get("holiday.template");
    private static final String WORKDAY_TEMPLATE = PropertyUtil.get("workday.template");
    private static final String[] NIAN_YUE_RI_ZHI_ETC = PropertyUtil.get("nian.yue.ri.zhi.etc").split(",");
    private static final int[] YEAR_MONTH_DATE = new int[NIAN_YUE_RI_ZHI_ETC.length];

    private EventUtil() {
    }

    /**
     * Get all holiday events, one day map to one event.
     *
     * @param holidayContent one line holiday content.
     * @return all holiday events.
     */
    public static List<Event> getEvents(List<String> holidayContent) {
        List<Event> events = new ArrayList<>();

        String name = holidayContent.get(0);
        // Add all holiday events.
        addEvents(events, HOLIDAY_TEMPLATE, name, holidayContent.get(1));
        // Add all workday events.
        addEvents(events, WORKDAY_TEMPLATE, name, holidayContent.get(3));

        return events;
    }

    /**
     * Get dates from holiday content, then create event from date and add to events list.
     *
     * @param events  the final events list.
     * @param content the holiday content.
     * @param name    the event name.
     */
    private static void addEvents(List<Event> events, String summaryTemplate, String name, String content) {
        List<LocalDate> dates = getDates(content);

        int cnt = 0;
        for (LocalDate date : dates) {
            cnt++;
            events.add(new Event(date, String.format(summaryTemplate, name, cnt, dates.size())));
        }
    }

    /**
     * Get all dates from holiday content.
     *
     * @param content the holiday content.
     * @return holiday or work dates.
     */
    private static List<LocalDate> getDates(String content) {
        String originContent = content;
        List<LocalDate> dates = new ArrayList<>();

        while (Arrays.stream(NIAN_YUE_RI_ZHI_ETC).anyMatch(content::contains)) {
            String[] ss;

            for (int i = 0; i < NIAN_YUE_RI_ZHI_ETC.length; i++) {
                ss = content.split(NIAN_YUE_RI_ZHI_ETC[i], 2);
                if (ss.length != 2) {
                    continue;
                }
                if (!ss[0].isEmpty()) {
                    YEAR_MONTH_DATE[i] = Integer.parseInt(ss[0]);
                }
                content = ss[1];
            }

            dates.add(LocalDate.of(YEAR_MONTH_DATE[0], YEAR_MONTH_DATE[1], YEAR_MONTH_DATE[2]));
        }

        return originContent.contains(NIAN_YUE_RI_ZHI_ETC[3]) ? expandDates(dates) : dates;
    }

    /**
     * Get all dates between origin dates.
     *
     * @param originDates the dates need to be expanded.
     * @return expanded dates.
     */
    private static List<LocalDate> expandDates(List<LocalDate> originDates) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate start = null;
        for (LocalDate date : originDates) {
            if (start != null) {
                dates.addAll(start.datesUntil(date).collect(Collectors.toList()));
            }
            start = date;
        }
        dates.add(start);

        return dates;
    }
}

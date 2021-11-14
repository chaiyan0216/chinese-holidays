package com.cy.holiday.util;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LinkUtil {

    private LinkUtil() {
    }

    /**
     * Get specific links from an url.
     *
     * @param url   the url need to visit.
     * @param reg   the specific pattern.
     * @param count the limit count.
     * @return links list.
     */
    public static List<String> getLinks(String url, String reg, int count) {
        Pattern pattern = Pattern.compile(reg);

        try {
            Elements links = Jsoup.connect(url).get().select("a[href]");
            return links.stream()
                    .map(l -> l.attr("abs:href"))
                    .filter(l -> pattern.matcher(l).matches())
                    .limit(count)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Get specific contents from an url.
     *
     * @param url the link need to visit.
     * @param reg the specific pattern.
     * @return specific content list.
     */
    public static List<List<String>> getContents(String url, String reg) {
        try {
            String text = Jsoup.connect(url).get().wholeText();
            return text.lines()
                    .map(l -> getGroups(l, reg))
                    .filter(ss -> !ss.isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Filter out each group and collect into a list.
     *
     * @param str the string need to check.
     * @param reg the group reg.
     * @return string list.
     */
    private static List<String> getGroups(String str, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);

        List<String> list = new ArrayList<>();

        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (!matcher.group(i).isEmpty()) {
                    list.add(matcher.group(i));
                }
            }
        }

        return list;
    }
}

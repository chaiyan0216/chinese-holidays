package com.cy.holiday.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LinkUtil {

    private LinkUtil() {
    }

    /**
     * Get specific contents from an url.
     *
     * @param url   the link need to visit.
     * @param reg   the specific pattern.
     * @param count the specific content count.
     * @return specific content list.
     */
    public static List<List<String>> getContents(String url, String reg, int count) {
        String response = getResponse(url);

        return response.lines()
                .map(s -> getGroups(s, reg))
                .filter(ss -> !ss.isEmpty())
                .limit(count > 0 ? count : Integer.MAX_VALUE)
                .collect(Collectors.toList());
    }

    public static List<List<String>> getContents(List<String> url, String reg) {
        return getContents(String.join("", url), reg, 0);
    }

    /**
     * Get html content from url.
     *
     * @param url the link need to visit.
     * @return the html content.
     */
    private static String getResponse(String url) {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return response == null ? "" : response.body();
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
                list.add(matcher.group(i));
            }
        }

        return list;
    }
}

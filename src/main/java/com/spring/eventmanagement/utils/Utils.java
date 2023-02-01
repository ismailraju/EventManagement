package com.spring.eventmanagement.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

   public static String[] getEventTime(Date start, Date end) {

        String ans = "";
        String pattern = "MMM-dd-yyyy-E-hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String startDateStr = simpleDateFormat.format(start);
        String[] startSplit = startDateStr.split("-");

        String endDateStr = simpleDateFormat.format(end);
        String[] endSplit = endDateStr.split("-");

        for (int i = 0; i < startSplit.length; i++) {
            if (!startSplit[i].equals(endSplit[i])) {
                startSplit[i] += " - " + endSplit[i];
            }
        }


        return startSplit;
    }
}

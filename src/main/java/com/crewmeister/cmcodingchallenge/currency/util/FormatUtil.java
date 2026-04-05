package com.crewmeister.cmcodingchallenge.currency.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A utility class that contains functions for performing data conversions (typically string to date or vice-versa)
 */
public class FormatUtil {

    /**
     * Converts a given string date (in yyyy-MM-dd format) to a `java.util.Date` object
     * @param date The string representation of the date (in yyyy-MM-dd) format
     * @return The `java.util.Date` representation of the date
     * @throws ParseException if the provided date string is in an invalid format
     */
    public static Date convertDateStringToCalendar(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        return dateFormat.parse(date);
    }
    
}

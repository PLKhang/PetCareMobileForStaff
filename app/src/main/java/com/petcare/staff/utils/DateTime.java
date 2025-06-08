package com.petcare.staff.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTime {
    private java.util.Date date;

    public DateTime(java.util.Date date) {
        this.date = date;
    }

    public java.util.Date getDate() {
        return date;
    }

    public static DateTime parse(String isoString) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            java.util.Date parsedDate = isoFormat.parse(isoString);
            return new DateTime(parsedDate);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid datetime format: " + isoString);
        }
    }

    public String toIsoString() {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return isoFormat.format(date);
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date);
    }

    public String toDateTimeString() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
        dateTimeFormat.setTimeZone(TimeZone.getDefault());
        return dateTimeFormat.format(date);
    }
}

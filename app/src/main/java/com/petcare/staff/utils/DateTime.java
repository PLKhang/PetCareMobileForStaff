package com.petcare.staff.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTime implements Comparable<DateTime>{
    private java.util.Date date;

    public DateTime() {
        this.date = new Date();
    }

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

    public static DateTime fromApiDateString(String dateString) {
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            apiFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date parsedDate = apiFormat.parse(dateString);
            return new DateTime(parsedDate);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid API date format: " + dateString);
        }
    }


    public String toApiDateString() {
        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        apiFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return apiFormat.format(date);
    }


    public static DateTime toDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            dateFormat.setTimeZone(TimeZone.getDefault());
            java.util.Date parsedDate = dateFormat.parse(dateString);
            return new DateTime(parsedDate);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + dateString);
        }
    }

    public static DateTime fromDateAndTime(String dateString, String timeString) {
        try {
            String combined = dateString + " " + timeString; // "dd/MM/yyyy HH:mm"
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
            dateTimeFormat.setTimeZone(TimeZone.getDefault());
            java.util.Date parsedDate = dateTimeFormat.parse(combined);
            return new DateTime(parsedDate);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date or time format: " + dateString + " " + timeString);
        }
    }
    @Override
    public int compareTo(DateTime other) {
        if (other == null || other.date == null) {
            return 1; // null coi là nhỏ hơn
        }
        return this.date.compareTo(other.date);
    }
}

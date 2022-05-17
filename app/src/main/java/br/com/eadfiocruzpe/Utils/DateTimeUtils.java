package br.com.eadfiocruzpe.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The class DateTimeUtils is responsible for handling various handy methods used on DateTime and
 * Date objects for parsing, conversion and formatting operations.
 */
public class DateTimeUtils {

    private static final String TIMEZONE = "UTC";
    private static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String PT_BR_PRETTY_DATETIME_FORMAT = "dd.MM.yyyy 'às' HH:mm";
    private static final String EPOCH_ZERO_ISO_TIMESTAMP = "1970-01-01T00:00:00Z";

    private LogUtils logUtils = new LogUtils();

    private Locale getPtBrLocale() {
        return new Locale ("pt", "BR");
    }

    private String getTimeStamp(Date date) {
        try {
            TimeZone tz = TimeZone.getTimeZone(DateTimeUtils.TIMEZONE);
            DateFormat df = new SimpleDateFormat(DateTimeUtils.ISO_DATETIME_FORMAT, Locale.US);
            df.setTimeZone(tz);
            return df.format(date);

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, "Failed to getTimeStamp");
            return DateTimeUtils.EPOCH_ZERO_ISO_TIMESTAMP;
        }
    }

    /**
     * Get a String object for the current time considering the user timezone and the ISO datetime format.
     */
    public String getNowTimeStamp() {
        try {
            return getTimeStamp(new Date());

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, "Failed to getNowTimeStamp");
            return DateTimeUtils.EPOCH_ZERO_ISO_TIMESTAMP;
        }
    }

    public Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);

        return calendar.getTime();
    }

    public Date getDate(String dateTimeStamp) {
        Date date = null;

        try {
            DateFormat df1 = new SimpleDateFormat(ISO_DATETIME_FORMAT, Locale.US);
            date = df1.parse(dateTimeStamp);

        } catch (ParseException pe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, "Could not get Date from timestamp");
        }

        return date;
    }

    public int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public String getPrettyDateForLocale(Date date, Locale locale) {
        DateFormat df = null;

        if (locale.equals(getPtBrLocale())) {
            TimeZone tz = TimeZone.getTimeZone(DateTimeUtils.TIMEZONE);
            df = new SimpleDateFormat(DateTimeUtils.PT_BR_PRETTY_DATETIME_FORMAT, getPtBrLocale());
            df.setTimeZone(tz);

        } else if (locale.equals(Locale.US)) {
            df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

        } else if (locale.equals(Locale.UK)) {
            df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);

        } else if (locale.equals(Locale.FRANCE)) {
            df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE);

        } else if (locale.equals(Locale.ITALY)) {
            df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ITALY);

        } else if (locale.equals(Locale.JAPAN)) {
            df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.JAPAN);
        }

        if (df != null) {
            return df.format(date);
        } else {
            df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        }

        return df.format(date);
    }

    public int getCalendarMonthFromInputMonth(String inputMonth) {
        inputMonth = inputMonth.toLowerCase();

        if (inputMonth.equals("jan")) {
            return Calendar.JANUARY;

        } else if (inputMonth.equals("feb")) {
            return Calendar.FEBRUARY;

        } else if (inputMonth.equals("mar")) {
            return Calendar.MARCH;

        } else if (inputMonth.equals("apr")) {
            return Calendar.APRIL;

        } else if (inputMonth.equals("may")) {
            return Calendar.MAY;

        } else if (inputMonth.equals("jun")) {
            return Calendar.JUNE;

        } else if (inputMonth.equals("jul")) {
            return Calendar.JULY;

        } else if (inputMonth.equals("aug")) {
            return Calendar.AUGUST;

        } else if (inputMonth.equals("sep")) {
            return Calendar.SEPTEMBER;

        } else if (inputMonth.equals("oct")) {
            return Calendar.OCTOBER;

        } else if (inputMonth.equals("nov")) {
            return Calendar.NOVEMBER;

        } else if (inputMonth.equals("dec")) {
            return Calendar.DECEMBER;

        } else {
            return -1;
        }
    }

    public int getCalendarWeekdayFromInputWeekday(String weekday) {
        weekday = weekday.toLowerCase();

        if (weekday.equals("mon")) {
            return Calendar.MONDAY;

        } else if (weekday.equals("tues")) {
            return Calendar.TUESDAY;

        } else if (weekday.equals("wed")) {
            return Calendar.WEDNESDAY;

        } else if (weekday.equals("thur")) {
            return Calendar.THURSDAY;

        } else if (weekday.equals("fri")) {
            return Calendar.FRIDAY;

        } else if (weekday.equals("sat")) {
            return Calendar.SATURDAY;

        } else if (weekday.equals("sun")) {
            return Calendar.SUNDAY;

        } else {
            return -1;
        }
    }

    public String getNameForDayOfWeek(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, "\n Couldn't return the name of a day of the week, please ensure you pass a valid date.");
            return "";
        }
    }

}
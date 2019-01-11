package utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String dateToFirstDayMonth(Date date) {
        return yearMonthToFirstDayMonth(date.getYear() + 1900, date.getMonth() + 1);
    }

    public static String yearMonthToLastDayMonth(int year, int month) {
        int tempYear = year;
        int tempMonth = month + 1;
        if (tempMonth > 12) {
            tempYear++;
            tempMonth -= 12;
        }
        return yearMonthToFirstDayMonth(tempYear, tempMonth);
    }

    public static String yearMonthToFirstDayMonth(int year, int month) {
        return month < 10 ? year + "-0" + month + "-01 00:00:00" : year + "-" + month + "-01 00:00:00";
    }

    public static String yearMonthDayToString(int year, int month, int day) {
        String monthStr = month < 10 ? "0" + month : "" + month;
        String dayStr = day < 10 ? "0" + day : "" + day;
        return year + "-" + month + "-" + day + " 00:00:00";
    }

    public static String dateAdd(Date date, int year, int month) {
        int tempYear = date.getYear() + year + 1900;
        int tempMonth = date.getMonth() + 1 + month;
        int tempDay = date.getDay();
        if (month > 12) {
            tempYear++;
            tempMonth -= 12;
        }
        if (month == 2 && tempDay > 28) {
            tempDay = 28;
        }
        return yearMonthDayToString(tempYear, tempMonth, tempDay);
    }

    public static String getStringByLength(String string, int length) {
        int len = string.length();
        for (int i = len; i < length; i++) {
            string += " ";
        }
        return string;
    }

    public static String formatDouble(double a) {
        return df.format(a);
    }
}

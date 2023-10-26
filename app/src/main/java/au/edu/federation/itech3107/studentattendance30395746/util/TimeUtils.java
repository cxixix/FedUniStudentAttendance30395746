package au.edu.federation.itech3107.studentattendance30395746.util;

import java.util.Calendar;
import java.util.Date;


public class TimeUtils {
    /**
     * Returns the actual number of weeks since the first week of a week
     *
     * @param weekBeginMillis
     * @param endMillis
     * @return
     */
    public static int getWeekGap(long weekBeginMillis, long endMillis) {
        return (int) (((endMillis - weekBeginMillis) / (1000 * 3600 * 24)) / 7);
    }

    /**
     * Obtain the date of Monday this week
     *
     * @return
     */
    public static Date getNowWeekBegin() {
        return getThisWeekMonday(new Date());
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // What day of the week is the current date obtained
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // Set the first day of the week, according to Chinese customs, the first day of the week is Monday
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // What day of the week is the current date obtained
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // According to the rules of the calendar, subtract the difference between the day of the week and the first day of the week from the current date
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * Get the current month
     * @return
     */
    public static int getNowMonth() {
        Calendar calendar = Calendar.getInstance();
        return 1 + calendar.get(Calendar.MONTH);
    }
}

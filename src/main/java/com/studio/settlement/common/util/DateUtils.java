package com.studio.settlement.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@Component
@Slf4j
public class DateUtils {

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYYMM = "yyyyMM";

    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取某个月的第x个星期y
     * @param year
     * @param month
     * @param weekOfMonth
     * @return
     */
    public static LocalDate getWeekdayOfMonth(int year, int month, int weekOfMonth, int dayOfWeek) {
        LocalDate localDate = LocalDate.of(year, month, 1);

        // 获取第一个星期y
        DayOfWeek tempDayOfWeek = DayOfWeek.MONDAY;
        switch (dayOfWeek){
            case 1:
                tempDayOfWeek = DayOfWeek.MONDAY;
                break;
            case 2:
                tempDayOfWeek = DayOfWeek.TUESDAY;
                break;
            case 3:
                tempDayOfWeek = DayOfWeek.WEDNESDAY;
                break;
            case 4:
                tempDayOfWeek = DayOfWeek.THURSDAY;
                break;
            case 5:
                tempDayOfWeek = DayOfWeek.FRIDAY;
                break;
            case 6:
                tempDayOfWeek = DayOfWeek.SATURDAY;
                break;
            case 7:
                tempDayOfWeek = DayOfWeek.SUNDAY;
                break;
            default:
                break;
        }

        LocalDate firstMondayOfMonth = localDate.with(TemporalAdjusters.firstDayOfMonth())
                .with(TemporalAdjusters.nextOrSame(tempDayOfWeek));

        LocalDate resultDate = null;
        if (weekOfMonth == 5) {
            // 最后一个，从当月最后一天倒序查找
            LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
            for (LocalDate date = lastDay; date.getMonthValue() == month; date = date.minusDays(1)) {
                if (date.getDayOfWeek().getValue() == dayOfWeek) {
                    resultDate = date;
                    break;
                }
            }
        } else {
            // 不是最后一个，直接加上7的倍数
            resultDate = firstMondayOfMonth.plusDays(7 * (weekOfMonth - 1));
        }

        return resultDate;
    }

    public static String getDateFormat(LocalDate localDate, String dateFormat) {
        return localDate.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static String getDateFormat(LocalDateTime localDateTime, String dateFormat) {
        return localDateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static LocalDate parseToLocalDate(String dateStr, String formatStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(formatStr));
    }

    public static LocalDateTime parseToLocalDateTime(String dateStr, String formatStr) {
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(formatStr));
    }

}

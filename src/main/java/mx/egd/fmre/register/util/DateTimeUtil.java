package mx.egd.fmre.register.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DateTimeUtil {
    public static LocalDateTime toLocalDateTime(Date date) {
        if(date == null) {
            log.warn("date is null");
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    public static LocalDate toLocalDate(Date date) {
        if(date == null) {
            log.warn("date is null");
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    
    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }
    
    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }
    
    public static Long diffInHours(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            log.error("start is null");
            return null;
        }
        if (end == null) {
            log.error("start is null");
            return null;
        }
        if (!start.isBefore(end)) {
            log.warn("start is not before end");
        }
        return ChronoUnit.HOURS.between(start, end);
    }
}

package warmup;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/** java.time. */
public final class DateUtil {
    private DateUtil() {}

    public static int ageOn(LocalDate birth, LocalDate on) {
        if (on.isBefore(birth)) {
            throw new IllegalArgumentException("date is before birth");
        }
        return Period.between(birth, on).getYears();
    }

    public static boolean isWeekend(LocalDate d) {
        DayOfWeek w = d.getDayOfWeek();
        return w == DayOfWeek.SATURDAY || w == DayOfWeek.SUNDAY;
    }

    public static String iso(LocalDate d) {
        return d.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

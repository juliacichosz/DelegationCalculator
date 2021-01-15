import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Calculator {

    long parseToDays(long hours) {
        return Duration.ofHours(hours).toDays();
    }

    long parseToHours(long minutes) {
        return Duration.ofMinutes(minutes).toHours();
    }

    long parseToMinutes(long hours) {
        return Duration.ofHours(hours).toMinutes();
    }

    long parseMinutesWithoutFullHours(long minutes) {
        long hours = parseToHours(minutes);
        long minutesOfFullHours = parseToMinutes(hours);
        return minutes - minutesOfFullHours;
    }

    long parseToHoursFromDays(long days) {
        return Duration.ofDays(days).toHours();
    }

    boolean anyFullDays(long hours, long minutes) {
        return (hours >= 12 && minutes != 0) || hours > 12;
    }

    boolean any8To12Hours(long hours, long minutes) {
        return hours >= 8 && minutes > 0;
    }

    boolean anyTo8Hours(long hours, long minutes) {
        return (hours < 8 && hours != 0) || (hours == 0 && minutes > 0) || (hours == 8 && minutes == 0);
    }

    BigDecimal countRateForFullDays(BigDecimal rate, long daysAmount) {
        return rate.multiply(new BigDecimal(String.valueOf(daysAmount)));
    }

    BigDecimal countRateFor8To12Hours(BigDecimal rate) {
        return rate.divide(new BigDecimal(2), RoundingMode.HALF_EVEN);
    }

    BigDecimal countRateForTo8Hours(BigDecimal rate) {
        return rate.multiply(new BigDecimal("0.3333"));
    }

    BigDecimal summaricRateForAllDays(long hours, long minutes, BigDecimal rate) {
        BigDecimal rateForFullDays;
        BigDecimal rateFor8To12Hours = BigDecimal.ZERO;
        BigDecimal rateForTo8Hour = BigDecimal.ZERO;
        BigDecimal totalRate = BigDecimal.ZERO;
        long daysAmount = parseToDays(hours);

        hours -= parseToHoursFromDays(daysAmount);
        if(anyFullDays(hours, minutes)) {
            daysAmount += 1;
            hours = 0;
            minutes = 0;
        }
        rateForFullDays = countRateForFullDays(rate, daysAmount);

        if(any8To12Hours(hours, minutes)) {
            rateFor8To12Hours = countRateFor8To12Hours(rate);
        }

        if(anyTo8Hours(hours, minutes)) {
            rateForTo8Hour = countRateForTo8Hours(rate);
        }

        return totalRate.add(rateForFullDays).add(rateFor8To12Hours).add(rateForTo8Hour);
    }

    BigDecimal calculateTotalRate(long totalTimeInMinutes, BigDecimal dailyRate) {
        if(totalTimeInMinutes == 0) {
            return (BigDecimal.ZERO).setScale(2, RoundingMode.HALF_EVEN);
        }

        long hours = parseToHours(totalTimeInMinutes);
        long minutes = parseMinutesWithoutFullHours(totalTimeInMinutes);

        return summaricRateForAllDays(hours, minutes, dailyRate).setScale(2, RoundingMode.HALF_EVEN);
    }

    long getZonedDateTimeDifferenceInMinutes(ZonedDateTime start, ZonedDateTime end){
        ChronoUnit unit = ChronoUnit.MINUTES;
        return unit.between(start, end);
    }

    static ZonedDateTime convertTimeFromStringToZonedDateTime(String time) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
        return ZonedDateTime.parse(time, formatter);
    }

    long calculateTimeOnDelegationInMinutes(String start, String end) {
        ZonedDateTime startZoned = convertTimeFromStringToZonedDateTime(start);
        ZonedDateTime endZoned = convertTimeFromStringToZonedDateTime(end);
        return getZonedDateTimeDifferenceInMinutes(startZoned, endZoned);
    }

    BigDecimal calculate(String start, String end, BigDecimal dailyRate) {
        long timeInMinutes = calculateTimeOnDelegationInMinutes(start, end);
        return calculateTotalRate(timeInMinutes, dailyRate);
    }
}
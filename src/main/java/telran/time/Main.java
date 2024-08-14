package telran.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

record Parameters(int month, int year, int firstDayOfWeekNumber) {
}

// Application can be used in a few modes:
// 1. Without parametrs: current month and year, first day of week - monday
// 2. With 2 parametrs: first parametr - year, second - month, first day of week - monday 
// 3. With 3 parametrs: first parametr - year, second - month, third - first day of week

public class Main {
    private static final int DAYS_IN_WEEK = 7;
    private static final int MONTHS_IN_YEAR = 12;
    
    public static void main(String[] args) {
        try {
            Parameters parameters = getParameters(args);
            printCalendar(parameters);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printCalendar(Parameters parameters) {
        printTitle(parameters);
        printWeekDays(parameters);
        printDates(parameters);
    }

    private static void printDates(Parameters parameters) {
        int current = 1;
        for (int i = 1; i <= getCountOfGaps(getWeekdayFirstOfMonth(parameters), parameters); i++) {
            System.out.print("    ");
            current++;
        }
        for (int i = 1; i <= getLastDayOfMonth(parameters); i++) {
            System.out.printf("%4d", i);
            current++;
            if (current > DAYS_IN_WEEK) {
                System.out.println();
                current = 1;
            }
        }
    }

    private static void printWeekDays(Parameters parameters) {
        for (int i = 1; i <= DAYS_IN_WEEK; i++) {
            System.out.printf("%4s", DayOfWeek.of(getDayOfWeekInAnotherWeek(i, parameters.firstDayOfWeekNumber())).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        }
        System.out.println();
    }

    private static void printTitle(Parameters parameters) {
        System.out.printf(" %4d %22s\n", parameters.year(), Month.of(parameters.month()).getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH));
    }

    private static Parameters getParameters(String[] args) throws Exception {
        LocalDate now = LocalDate.now();
        int month = now.get(ChronoField.MONTH_OF_YEAR);
        int year = now.get(ChronoField.YEAR);
        int firstDayOfWeekNumber = 1;
        if (args.length == 1 || args.length > 3) {
            throw new Exception("You can use tha application with 2, 3 or without parametrs!");
        }
        if (args.length == 2 || args.length == 3) {
            try {
                for (int i = 0; i < args.length; i++) {
                    if (i == 0) {
                        year = Integer.valueOf(args[i]);
                    }
                    if (i == 1) {
                        month = Integer.valueOf(args[i]);
                    }
                    if (i == 2) {
                        firstDayOfWeekNumber = Integer.valueOf(args[i]);
                    }
                }                                
            } catch (NumberFormatException e) {
                throw new Exception("Year or month must be numbers!");
            }
        }
        if (year <= 0) {
            throw new Exception("Year must be grater than 0!");
        }
        if (month < 1 || month > MONTHS_IN_YEAR) {
            throw new Exception(String.format("Month must be in range from 1 to %d!", MONTHS_IN_YEAR));
        }
        if (firstDayOfWeekNumber < 1 || firstDayOfWeekNumber > DAYS_IN_WEEK) {
            throw new Exception(String.format("Firts day of week must be in range from 1 to %d!", DAYS_IN_WEEK));
        }
        return new Parameters(month, year, firstDayOfWeekNumber);
    }

    private static DayOfWeek getWeekdayFirstOfMonth(Parameters parameters) {
        LocalDate firstDay = LocalDate.of(parameters.year(), parameters.month(), 1);
        return DayOfWeek.of(firstDay.get(ChronoField.DAY_OF_WEEK));
    }

    private static int getCountOfGaps(DayOfWeek dayOfWeek, Parameters parameters) {
        return DAYS_IN_WEEK - getDayOfWeekInAnotherWeek(dayOfWeek.getValue(), parameters.firstDayOfWeekNumber());
    }

    private static int getLastDayOfMonth(Parameters parameters) {
        int month = parameters.month();
        int year = parameters.year();
        LocalDate nextMonthFirstDay = (month == MONTHS_IN_YEAR) ? LocalDate.of(year + 1, 1, 1) : LocalDate.of(year, month + 1, 1);
        return (nextMonthFirstDay.minus(1, ChronoUnit.DAYS)).get(ChronoField.DAY_OF_MONTH);
    }

    private static int getDayOfWeekInAnotherWeek(int day, int firstDayOfWeekNumber) { 
        int result = day + firstDayOfWeekNumber - 1;
        return result > DAYS_IN_WEEK ? result - DAYS_IN_WEEK : result;
    }
}
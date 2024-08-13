package telran.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

record MonthYear(int month, int year) {
}

public class Main {
    public static void main(String[] args) {
        try {
            MonthYear monthYear = getMonthYear(args);
            printCalendar(monthYear);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printCalendar(MonthYear monthYear) {
        printTitle(monthYear);
        printWeekDays(monthYear);
        printDates(monthYear);
    }

    private static void printDates(MonthYear monthYear) {
        int current = 1;
        for (int i = 1; i <= getOffset(getFirstDayOfWeek(monthYear)); i++) {
            System.out.print("    ");
            current++;
        }
        for (int i = 1; i <= getLastDayOfMonth(monthYear); i++) {
            System.out.printf("%4d", i);
            current++;
            if (current > 7) {
                System.out.println();
                current = 1;
            }
        }
        System.out.println();
    }

    private static void printWeekDays(MonthYear monthYear) {
        for (int i = 1; i <= 7; i++) {
            System.out.printf("%4s", DayOfWeek.of(i).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        }
        System.out.println();
    }

    private static void printTitle(MonthYear monthYear) {
        System.out.printf(" %4d %22s\n", monthYear.year(), Month.of(monthYear.month()).toString());
    }

    private static MonthYear getMonthYear(String[] args) throws Exception {
        LocalDate now = LocalDate.now();
        int month = now.get(ChronoField.MONTH_OF_YEAR);
        int year = now.get(ChronoField.YEAR);
        if (args.length == 1 || args.length > 2) {
            throw new Exception("You can use tha application with 2 parametrs or without parametrs!");
        }
        if (args.length == 2) {
            try {
                year = Integer.valueOf(args[0]);
                month = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
                throw new Exception("Year or month must be numbers!");
            }
            if (year <= 0) {
                throw new Exception("Year must be grater than 0!");
            }
            if (month < 1 || month > 12) {
                throw new Exception("Month must be in range from 1 to 12!");
            }
        }
        return new MonthYear(month, year);
    }

    private static DayOfWeek getFirstDayOfWeek(MonthYear monthYear) {
        LocalDate firstDay = LocalDate.of(monthYear.year(), monthYear.month(), 1);
        return DayOfWeek.of(firstDay.get(ChronoField.DAY_OF_WEEK));
    }

    private static int getOffset(DayOfWeek dayOfWeek) {
        return dayOfWeek.getValue() - 1;
    }

    private static int getLastDayOfMonth(MonthYear monthYear) {
        int month = monthYear.month();
        int year = monthYear.year();
        LocalDate nextMonthFirstDay = month == 12 ? LocalDate.of(year + 1, 1, 1) : LocalDate.of(year, month + 1, 1);
        return (nextMonthFirstDay.minus(1, ChronoUnit.DAYS)).get(ChronoField.DAY_OF_MONTH);
    }
}
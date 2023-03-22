package seedu.apollo.calendar;

import seedu.apollo.exception.utils.InvalidSaveFile;
import seedu.apollo.module.CalendarModule;
import seedu.apollo.module.Module;
import seedu.apollo.module.Timetable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Calendar class that stores the modules user is taking in a 2D ArrayList.
 */

public class Calendar extends ArrayList<ArrayList<CalendarModule>> {

    /**
     *  Constructor for Calendar.
     */
    public Calendar() {
        super(7);
        for (int i = 0; i < 7; i++) {
            this.add(new ArrayList<>());
        }
    }
    /**
     * Adds a module to the calendar.
     *
     * @param module The module to be added.
     * @throws InvalidSaveFile If the module timetable is invalid.
     */

    public void addModule(Module module) throws InvalidSaveFile {

        try {
            for (Timetable timetable : module.getModuleTimetable()) {
                CalendarModule calendarModule = new CalendarModule(module.getCode(),
                        module.getTitle(), module.getModuleCredits());

                calendarModule.setSchedule(timetable);
                int index = calendarModule.getDayIndex();
                if (index == -1) {
                    throw new InvalidSaveFile();
                }

                this.get(index).add(calendarModule);
                this.sortDayByTime(this.get(index));

            }
        } catch (NullPointerException e) {
            return;
        }

    }

    /**
     * Creates an empty calendar
     */
    public void clearCalendar() {
        for (int i = 0; i < 7; i++) {
            this.get(i).clear();
        }
    }

    /**
     * Sorts the lessons in a day by start time and end time.
     *
     * @param day The array list containing lessons for the day.
     */
    private void sortDayByTime(ArrayList<CalendarModule> day) {
        day.sort((CalendarModule lesson1, CalendarModule lesson2) -> {
            SimpleDateFormat format = new SimpleDateFormat("HHmm");
            try {
                Date startTime1 = format.parse(lesson1.getSchedule().getStartTime());
                Date endTime1 = format.parse(lesson1.getSchedule().getEndTime());

                Date startTime2 = format.parse(lesson2.getSchedule().getStartTime());
                Date endTime2 = format.parse(lesson2.getSchedule().getEndTime());

                return this.determineSort(startTime1, endTime1, startTime2, endTime2);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Determines the order of the lessons based on start time and end time.
     *
     * @param startTime1 The start time of the first lesson.
     * @param endTime1 The end time of the first lesson.
     * @param startTime2 The start time of the second lesson.
     * @param endTime2 The end time of the second lesson.
     * @return The order of the lessons.
     */
    private int determineSort(Date startTime1, Date endTime1, Date startTime2, Date endTime2) {
        // if both lessons start at the same time
        if (startTime1.equals(startTime2)) {
            // lesson2 ends first
            if (endTime1.after(endTime2)) {
                return 1;
                // lesson1 ends first
            } else if (endTime1.before(endTime2)) {
                return -1;
                // Both lessons end at the same time
            } else {
                return 0;
            }
        } else if (startTime1.after(startTime2)) {
            return 1;
        } else {
            return -1;
        }
    }

}



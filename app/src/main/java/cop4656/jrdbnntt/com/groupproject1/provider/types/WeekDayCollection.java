package cop4656.jrdbnntt.com.groupproject1.provider.types;

import android.util.SparseBooleanArray;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A serialized collection of weekdays for toggling options per day.
 *
 * Maps day of week to boolean
 */

public class WeekDayCollection extends SQLiteSerializedType {

    public enum WeekDay {
        SUNDAY(Calendar.SUNDAY, "Su"),
        MONDAY(Calendar.MONDAY, "Mo"),
        TUESDAY(Calendar.TUESDAY, "Tu"),
        WEDNESDAY(Calendar.WEDNESDAY, "We"),
        THURSDAY(Calendar.THURSDAY, "Th"),
        FRIDAY(Calendar.FRIDAY, "Fr"),
        SATURDAY(Calendar.SATURDAY, "Sa");

        private int id;
        private String shortString;

        WeekDay(int id, String shortString) {
            this.id = id;
            this.shortString = shortString;
        }

        public int getId() {
            return this.id;
        }

        public String getShortString() {
            return this.shortString;
        }
    }

    private Map<WeekDay, Boolean> toggledDays = new HashMap<>(7);


    public WeekDayCollection(String serialization) throws ParseException {
        deserialize(serialization);
    }

    public WeekDayCollection() {
        for (WeekDay day : WeekDay.values()) {
            toggledDays.put(day, false);
        }
    }

    @Override
    public String serialize() {
        String serialization = "";

        for (WeekDay day : WeekDay.values()) {
            if (toggledDays.get(day)) {
                serialization += day.getShortString();
            }
        }

        return serialization;
    }

    @Override
    protected void deserialize(String serialization) throws ParseException {
        for (WeekDay day : WeekDay.values()) {
            toggledDays.put(day, serialization.contains(day.getShortString()));
        }
    }


    public void setDay(WeekDay day, boolean value) {
        toggledDays.put(day, value);
    }

    public boolean getDay(WeekDay day) {
        return toggledDays.get(day);
    }

    public List<WeekDay> getEnabledDays() {
        List<WeekDay> days = new ArrayList<>();

        for (Map.Entry<WeekDay, Boolean> entry : toggledDays.entrySet()) {
            if (entry.getValue()) {
                days.add(entry.getKey());
            }
        }

        return days;
    }


    public boolean includes(Calendar calendarDay) {
        int checkDay = calendarDay.get(Calendar.DAY_OF_WEEK);
        for (WeekDay day : WeekDay.values()) {
            if (day.getId() == checkDay && this.getDay(day)) {
                return true;
            }
        }
        return false;
    }
}

package cop4656.jrdbnntt.com.groupproject1.provider.table;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import cop4656.jrdbnntt.com.groupproject1.CourseAlarmReceiver;
import cop4656.jrdbnntt.com.groupproject1.MainActivity;
import cop4656.jrdbnntt.com.groupproject1.R;
import cop4656.jrdbnntt.com.groupproject1.provider.types.Time;
import cop4656.jrdbnntt.com.groupproject1.provider.types.WeekDayCollection;


public class Course extends DatabaseTable {

    // TODO add to prefs
    private static final String DEFAULT_ALARM_BUFFER = "30";

    // Database
    public static final String TABLE_NAME = "course";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ROOM = "room";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_DAYS = "days";

    // Column instance usage
    public String name;
    public String room;
    public Time startTime;
    public WeekDayCollection days;

    public Course() {}

    @Override
    public String getCreateSql() {
        return "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME + " TEXT,"
                + COLUMN_ROOM + " TEXT,"
                + COLUMN_START_TIME + " TEXT,"
                + COLUMN_DAYS + " TEXT);";
    }


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    private PendingIntent getAlarmIntent(Context context) {
        Intent intent = CourseAlarmReceiver.newAlarmIntent(context, this.id);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private List<Long> calcAlarmTimes(int alarmBufferMinutes) {
        List<Long> triggerTimes = new ArrayList<>();


        for (WeekDayCollection.WeekDay day : days.getEnabledDays()) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, day.getId());
            calendar.set(Calendar.HOUR, startTime.getCalendar().get(Calendar.HOUR));
            calendar.set(Calendar.MINUTE, startTime.getCalendar().get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.MINUTE, -1*alarmBufferMinutes);
            triggerTimes.add(calendar.getTimeInMillis());
        }

        return triggerTimes;
    }

    public void enable(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getAlarmIntent(context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int alarmBufferMinutes = Integer.parseInt(
                preferences.getString("alarmBuffer", DEFAULT_ALARM_BUFFER)
        );

        for (Long triggerTime : calcAlarmTimes(alarmBufferMinutes)) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, AlarmManager.INTERVAL_DAY * 7, pendingIntent);

            // Log new alarm
            SimpleDateFormat dateFormat = new SimpleDateFormat("E yyyy-MM-dd h:mm a", Locale.getDefault());
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(new Date(triggerTime));
            Log.i("ALARM_SET", name + " @ " + dateFormat.format(calendar.getTime()));
        }

    }

    public void disable(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getAlarmIntent(context);
        alarmManager.cancel(pendingIntent);
    }

}

package cop4656.jrdbnntt.com.groupproject1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cop4656.jrdbnntt.com.groupproject1.provider.MyContentProvider;
import cop4656.jrdbnntt.com.groupproject1.provider.table.Course;
import cop4656.jrdbnntt.com.groupproject1.provider.types.Time;

/**
 * TODO
 */
public class CourseAlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 124462;
    private static final String ARG_COURSE_ID = "courseId";

    public static Intent newAlarmIntent(Context context, long courseId) {
        Intent intent = new Intent();
        intent.setClass(context, CourseAlarmReceiver.class);
        intent.setAction(context.getString(R.string.action_ALARM));
        intent.putExtra(ARG_COURSE_ID, courseId);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Verify intent
        if (!intent.getAction().equals(context.getString(R.string.action_ALARM))) {
            Log.e(this.getClass().getName(), "Invalid intent action: " + intent.getAction());
            return;
        }

        // Get course name + location information from DB (using course id from bundle)
        Bundle bundle = intent.getExtras();
        if (bundle == null || bundle.get(ARG_COURSE_ID) == null) {
            Log.e(this.getClass().getName(), "Missing or invalid bundle");
            return;
        }
        Course course = getCourse(context, bundle.getLong(ARG_COURSE_ID));
        if (course == null) {
            Log.e(this.getClass().getName(), "Unable to retrieve course");
            return;
        }

        // Format Start Time
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm", Locale.getDefault());
        String startTime = dateFormat.format(course.startTime.getCalendar().getTime());

        // Estimate navigation time in minutes
        long minuteDistance = getMinuteDistanceEstimate(course.room);

        // Display notification
        Intent clickIntent = NavigateToCourseActivity.newNavigateIntent(context, course.id);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(course.name + " starts at " + startTime)
                .setContentText("You are "+minuteDistance+" minutes away")
                .addAction(buildViewScheduleAction(context, course));

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private @Nullable Course getCourse(Context context, long id) {
        Course course = new Course();
        course.id = id;

        // Select name, room. and start time by id
        Cursor cursor = context.getContentResolver().query(
                MyContentProvider.getUriForTable(Course.TABLE_NAME),
                new String [] {
                        Course.COLUMN_NAME,
                        Course.COLUMN_ROOM,
                        Course.COLUMN_START_TIME
                },
                Course.COLUMN_ID + " = ?",
                new String [] {
                        course.id.toString()
                },
                null
        );

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToNext();
        course.name = cursor.getString(0);
        course.room = cursor.getString(1);

        try {
            course.startTime = new Time(cursor.getString(2));
        } catch (ParseException e) {
            Log.e(this.getClass().getName(), "Unable to parse start time from db object");
            return null;
        }

        cursor.close();

        return course;
    }


    private long getMinuteDistanceEstimate(String room) {
        return 0;
    }

    private NotificationCompat.Action buildViewScheduleAction(Context context, Course course) {
        Intent intent = new Intent(context, CoursesListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action.Builder builder = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher, "View Schedule", pendingIntent);

        return builder.build();
    }

}

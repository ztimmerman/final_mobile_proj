package cop4656.jrdbnntt.com.groupproject1;

import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cop4656.jrdbnntt.com.groupproject1.provider.MyContentProvider;
import cop4656.jrdbnntt.com.groupproject1.provider.table.Course;
import cop4656.jrdbnntt.com.groupproject1.provider.types.Time;
import cop4656.jrdbnntt.com.groupproject1.provider.types.WeekDayCollection;

/**
 * Created by Cristian Palencia on 4/4/2017.
 */

public class WidgetAdapter implements RemoteViewsService.RemoteViewsFactory {
    private Context context;

    private ArrayList<String> courseDescriptors = new ArrayList<>();

    public WidgetAdapter(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate() {
        Cursor cursor = context.getContentResolver().query(
                MyContentProvider.getUriForTable(Course.TABLE_NAME),
                new String [] {
                        Course.COLUMN_NAME,
                        Course.COLUMN_START_TIME,
                        Course.COLUMN_DAYS
                },
                null,
                null,
                null
        );

        Course course;
        Calendar today = GregorianCalendar.getInstance();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    course = new Course();
                    course.name = cursor.getString(0);
                    course.startTime = new Time(cursor.getString(1));
                    course.days = new WeekDayCollection(cursor.getString(2));

                    if (course.days.includes(today)) {
                        courseDescriptors.add(course.name + " @ " + course.startTime.serialize());
                    }
                } catch (ParseException e) {
                    // Ignore
                }
            }
            cursor.close();
        }
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return courseDescriptors.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.list_item);
        rv.setTextViewText(R.id.textView, courseDescriptors.get(i));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

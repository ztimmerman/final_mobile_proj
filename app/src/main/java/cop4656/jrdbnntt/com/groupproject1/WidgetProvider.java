package cop4656.jrdbnntt.com.groupproject1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Cristian Palencia on 4/4/2017.
 */

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int []realAppWidgetIds=AppWidgetManager.
                getInstance(context).getAppWidgetIds(new ComponentName(context,WidgetProvider.class));

        for (int id: realAppWidgetIds)
        {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);


            Intent serviceIntent = new Intent(context, WidgetService.class);
            remoteViews.setRemoteAdapter(R.id.listView, serviceIntent);

            Intent intent = new Intent(context,WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,realAppWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            remoteViews.setOnClickPendingIntent(R.id.frameLayout,pendingIntent);

            // On click open schedule
            PendingIntent pendingScheduleIntent = PendingIntent.getActivity(
                    context,
                    0,
                    new Intent(context, CoursesListActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            remoteViews.setOnClickPendingIntent(R.id.bViewSchedule, pendingScheduleIntent);

            appWidgetManager.updateAppWidget(id, remoteViews);

        }
    }
}

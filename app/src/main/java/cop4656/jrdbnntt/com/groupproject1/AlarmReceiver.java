package cop4656.jrdbnntt.com.groupproject1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String noteTitle = "Map O Shame";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        NotificationCompat.Builder myBuilder;
        Intent outboundIntent;
        PendingIntent outPendIntent;
        Bundle outBundle;
        NotificationManager myManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        //Data will be received from intent for purposes of seinding to notification
        Bundle bundle = intent.getExtras();
        //notification shouldn't run if there's no data.
        if (bundle != null) {
            outboundIntent = new Intent(context,ShameActivity.class);
            outBundle = new Bundle();
            outBundle.putString("class",bundle.getString("class"));
            outPendIntent = PendingIntent.getActivity(context,0,outboundIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            myBuilder = new NotificationCompat.Builder(context);
            myBuilder.setContentTitle(noteTitle);
            myBuilder.setContentText("You may be late for " + bundle.getString("class"));
            myBuilder.setSmallIcon(R.drawable.ic_search_black_24dp);
            myBuilder.build();

            int mNotificationID = 1;


        }





        //throw new UnsupportedOperationException("Not yet implemented");
    }
}

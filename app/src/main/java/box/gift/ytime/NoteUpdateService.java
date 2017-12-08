package box.gift.ytime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class NoteUpdateService extends Service {
    private boolean alreadyStarted = false;
    private Context context;
    private NotificationManager mNotifyMgr;
    private int notificationId = 7;
    private boolean foregroundStarted = false;
    private NotificationCompat.Builder mBuilder;
    private NotificationFormater.NotificationParts notificationText;
    private ScheduleEngine eng;
    private Timer updateTimer;
    private boolean justNotified = false;
    public static long CLASS_PROXIMITY = 1000 * 60 * 3; //3 minutes

    public NoteUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //Only run the first time (this may be already running for some reason, e.g. the startup broadcast reciever, or it was not shut down properly last time
        if (alreadyStarted) return START_STICKY;
        alreadyStarted = true;

        context = getApplicationContext();

        //Create notification builder and make it persist
        mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.bookclock);
        mBuilder.setOngoing(true); //Cannot be swiped away
        mBuilder.setShowWhen(false); //Do not show the time of issuing the notification (that is irrelevant and confusing for this purpose)
        mBuilder.setPriority(Notification.PRIORITY_LOW);
                //.addAction(R.drawable.note, "close", PendingIntent.ge);

        // Gets an instance of the NotificationManager service
        mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Every second, update the notification
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Log.d(m, "update");
                updateNotification();
            }
        }, 1000, 1000);

        return START_STICKY;
    }

    //Gets the notification text and updates the notification
    private void updateNotification() {
        eng = new ScheduleEngine(context);
        notificationText = eng.getNotification();
        mBuilder
                .setContentTitle(notificationText.getTitle()) //Looks better to put it all in the title
                //.setContentText(notificationText.getBody()) //Reserve body for additional info
                .setSubText(notificationText.getContext()); //Current class in the sub text spot

        if (justNotified)
        {
            mBuilder.setPriority(Notification.PRIORITY_LOW);
            mBuilder.setVibrate(new long[] {0});
            mBuilder.setSound(null);
            justNotified = false;
        }

        SharedPreferences sharedPref = context.getSharedPreferences("SettingsYTime", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("notify", false)) //If we have notify set, then...
        {
            if (notificationText.getTimeUntil() == CLASS_PROXIMITY) //If we are exactly a certain amount of time from class, raise an alert with vibration and sound
            {
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                mBuilder.setVibrate(new long[] { 0, 600, 200, 400 });
                mBuilder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notesound));
                justNotified = true;
            }
        }

        //Log.d("h", CLASS_PROXIMITY + " " + notificationText.getTimeUntil());
        if (!foregroundStarted) //Not sure if it really makes a difference whther thi is foreground or not, because it needs a notification either way, but perhaps marking it as a foreground service will make the system less likely to garbage collect
        {
            foregroundStarted = true;
            startForeground(notificationId, mBuilder.build());
        }
        else
        {
            mNotifyMgr.notify(notificationId, mBuilder.build());
        }
    }

    //When this is destroyed, gracefully cancel the notification
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d("A: ", "destroyed");
        updateTimer.cancel();
        mNotifyMgr.cancelAll();
        stopForeground(false);
    }
}

/*
*    This file is part of GPSLogger for Android.
*
*    GPSLogger for Android is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 2 of the License, or
*    (at your option) any later version.
*
*    GPSLogger for Android is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with GPSLogger for Android.  If not, see <http://www.gnu.org/licenses/>.
*/

//TODO: Simplify email logic (too many methods)

package net.dgistudio.guillaume.opendnsupdater;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.bosphere.filelogger.FL;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

public class openDnsService extends Service {
    public int counter=0;
    private static NotificationManager notificationManager;
    private static int NOTIFICATION_ID = 8675310;
    private final IBinder binder = new GpsLoggingBinder();
    private NotificationCompat.Builder nfc = null;
    private onNetworkChange opendnsReceiver;

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        opendnsReceiver = new onNetworkChange();
        registerReceiver(opendnsReceiver, intentFilter);
        FL.i ("NewService:======>", true+"");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FL.i ("NewService:======>", "OnStartCmd");
        HandleIntent(intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(opendnsReceiver);
        RemoveNotification();
        super.onDestroy();
        FL.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("net.dgistudio.guillaume.opendnsupdater.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    /*@Override
    public void onLowMemory() {
        super.onLowMemory();
    }*/

    private void HandleIntent(Intent intent) {
        if (intent != null) {
            /*Bundle bundle = intent.getExtras();
            if (bundle != null) {

            }*/
        } else
            {
            // A null intent is passed in if the service has been killed and restarted.
            //StartLogging();
        }
    }

    protected void StartLogging() {
        /*if (Session.isStarted())
        {
            ShowNotification();//MJH
            return;
        }*/

        try {
            //startForeground(NOTIFICATION_ID, new Notification());
        } catch (Exception ex) {
        }
        //Session.setStarted(true);
        ShowNotification();
        startTimer();
        FL.i ("NewService:======>", "Intent_Null");
    }

    /**
     * Stops logging, removes notification, stops GPS manager, stops email timer
     */
    public void StopLogging() {
        //Session.setStarted(false);
        stopForeground(true);
        RemoveNotification();
    }

    /**
     * Hides the notification icon in the status bar if it's visible.
     */
    private void RemoveNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    /**
     * Shows a notification icon in the status bar for GPS Logger
     */
    private void ShowNotification() {
        // What happens when the notification item is clicked
        Intent contentIntent = new Intent(this, BasicSetting.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(contentIntent);

        PendingIntent pending = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        NumberFormat nf = new DecimalFormat("###.#####");

        String contentText = "App is running";
        long notificationTime = System.currentTimeMillis();
        //if (nfc == null)
        {
            int idSmall=R.drawable.opendns,
                    idLarge=R.drawable.opendns;

            nfc = new NotificationCompat.Builder(getApplicationContext())
                    //.setSmallIcon(R.drawable.notification)
                    //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.gpsloggericon3))
                    .setSmallIcon(idSmall)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), idLarge))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(contentText)
                    .setOngoing(true)
                    .setContentIntent(pending);

        }



        nfc.setContentTitle(contentText);
        nfc.setContentText(getString(R.string.app_name));
        nfc.setWhen(notificationTime);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, nfc.build());
    }
    /**
     * Can be used from calling classes as the go-between for methods and
     * properties.
     */
    public class GpsLoggingBinder extends Binder {
        public openDnsService getService() {
            return openDnsService.this;
        }
    }
    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                FL.i("in timer", "in timer ++++  "+ (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}

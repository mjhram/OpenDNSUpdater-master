package net.dgistudio.guillaume.opendnsupdater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bosphere.filelogger.FL;

/**
 * Created by hp on 4/15/2018.
 */

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        FL.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, openDnsService.class));;
    }
}

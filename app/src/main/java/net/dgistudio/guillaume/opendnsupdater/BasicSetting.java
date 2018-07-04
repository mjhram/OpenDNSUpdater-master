package net.dgistudio.guillaume.opendnsupdater;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


public class BasicSetting extends PreferenceActivity /*implements setPasswordDialog.NoticeDialogListener, welcomeTextDialog.NoticeDialogListener*/{
    private Intent mServiceIntent;
    private openDnsService mSensorService;
    private Context ctx;
    public Context getCtx() {
        return getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

       if (prefs.getBoolean("firstTime",true))
        {
            //DialogFragment firstime = new welcomeTextDialog();
            //firstime.show(this.getFragmentManager(), "welcome");

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();
       }
        addPreferencesFromResource(R.xml.pref_general);

        ctx = this;


        mSensorService = new openDnsService();
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }
    }

    /*@Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog.getTag().equals("setPass"))
        {
            Log.d("d", "Dialog pass validated");
        }
        else if (dialog.getTag().equals("welcome"))
        {
            DialogFragment setPass = new setPasswordDialog();
            setPass.show(this.getFragmentManager(), "setPass");
       }
    }*/

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.i ("isMyServiceRunning?", service.service.getClassName());
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();
    }

    /*@Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }*/


    //TODO : check validity on every config change
    //TODO : Force use of openDNS DNS servers
    //TODO : display a version Number
    //TODO : Add a password befor edit
}
package net.dgistudio.guillaume.opendnsupdater;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.bosphere.filelogger.FL;

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

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_PERMISSION);
        }*/
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
            FL.d("d", "Dialog pass validated");
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
            FL.i ("isMyServiceRunning?", service.service.getClassName());
            if (serviceClass.getName().equals(service.service.getClassName())) {
                FL.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        FL.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        FL.i("MAINACT", "onDestroy!");
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
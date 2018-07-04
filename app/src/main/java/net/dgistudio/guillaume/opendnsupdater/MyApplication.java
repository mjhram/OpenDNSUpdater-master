package net.dgistudio.guillaume.opendnsupdater;

import android.app.Application;
import android.os.Environment;

import com.bosphere.filelogger.FL;
import com.bosphere.filelogger.FLConfig;
import com.bosphere.filelogger.FLConst;

import java.io.File;

/**
 * Created by hp on 4/16/2018.
 */

public class MyApplication extends Application
{
    private static MyApplication instance;

    public static synchronized MyApplication getAppInstance() { return instance; }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;

        File file = new File(Environment.getExternalStorageDirectory(), "file_logger_demo");
        FL.init(new FLConfig.Builder(this)
                //.logger(...)       // customise how to hook up with logcat
        //.defaultTag("Default Tag")   // customise default tag
            .minLevel(FLConst.Level.V)   // customise minimum logging level
            .logToFile(true)   // enable logging to file
            .dir(file)    // customise directory to hold log files
            //.formatter(...)    // customise log format and file name
            .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT) // customise retention strategy
            .maxFileCount(FLConst.DEFAULT_MAX_FILE_COUNT)    // customise how many log files to keep if retention by file count
            .maxTotalSize(FLConst.DEFAULT_MAX_TOTAL_SIZE)    // customise how much space log files can occupy if retention by total size
            .build());
        FL.setEnabled(true);
    }
}
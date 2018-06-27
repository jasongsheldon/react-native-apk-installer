package com.cnull.apkinstaller;

import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import java.io.File;

public class ApkInstallerModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext _context = null;
    private final String Tag = "APkLoader";
    // private static final String DURATION_SHORT_KEY = "SHORT";
    // private static final String DURATION_LONG_KEY = "LONG";

    public ApkInstallerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        _context = reactContext;
    }

    @Override
    public String getName() {
        return "ApkInstaller";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        // constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        // constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    // public void show(String message, int duration) {
    public void test(String message) {
        Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @ReactMethod
    public void install(String path) {
        String cmd = "chmod 777 " +path;
        Uri myuri;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            myuri = Uri.parse("file://"+path);
        } else {
            Log,d(Tag,"path is " + path);
            File o = new File(path);
            Log,d(Tag,"o is " + o);
            if (o.exists()) {
                Log,d(Tag,"file does exist");
                Log,d(Tag,"o.getName()" + o.getName());
            }


            Log,d(Tag,"_context is " + _context);
            Log,d(Tag,"_context.getApplicationContext().getPackageName() + \".provider\" is " + _context.getApplicationContext().getPackageName() + ".provider");
            myuri = FileProvider.getUriForFile(_context, _context.getApplicationContext().getPackageName() + ".provider", o);
            Log,d(Tag,"myuri is " + myuri);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(myuri, "application/vnd.android.package-archive");
        _context.startActivity(intent);
    }
}

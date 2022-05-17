package br.com.eadfiocruzpe.Utils;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.util.List;

public class PermissionUtils {

    public static final int IDX_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;

    private static boolean sExternalStoragePermission = false;
    private static boolean sHasAskedExternalStoragePermission = false;

    /**
     * Permission is automatically granted on sdk < 23 upon installation
     */
    public boolean isStoragePermissionGranted(final Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Checks if external storage is available for read and write
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isAppInstalled(Context context, String uri) {
        PackageManager packageManager = context.getPackageManager();
        boolean isAppInstalled;

        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            isAppInstalled = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            isAppInstalled = false;
        }

        return isAppInstalled;
    }

    /**
     * If request is cancelled, the result arrays are empty.
     */
    public static void confirmExternalStoragePermissionGranted() {
        sExternalStoragePermission = true;
    }

    public static void setExternalStoragePermissionGranted(int[] grantResults) {
        sExternalStoragePermission = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean wasExternalStoragePermissionGranted() {
        return sExternalStoragePermission;
    }

    public static void setHasAskedExternalStoragePermission(boolean hasAskedPermission) {
        sHasAskedExternalStoragePermission = hasAskedPermission;
    }

    public static boolean hasAskedExternalStoragePermission() {
        return sHasAskedExternalStoragePermission;
    }

}
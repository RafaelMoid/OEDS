package br.com.eadfiocruzpe.Utils;

import android.content.Context;
import android.util.Log;

import java.io.*;

/**
 * The LogUtils class is responsible for creating logs that are used across the app and for
 * external analytics tools.
 */
public class LogUtils {

    private static final String LOG_FILENAME = "system_log.txt";

    private PermissionUtils permissionUtils = new PermissionUtils();
    private Context mContext = null;

    public LogUtils() {}

    public LogUtils(Context context) {
        mContext = context;
    }

    /**
     * Writes a log message in to the console and into the log file
     */
    public void logMessage(TypeUtils.LogMsgType messageType, String message) {
        try {

            if (messageType.equals(TypeUtils.LogMsgType.TEST)) {
                logSystemMessage(messageType, message);

            } else {

                if (mContext != null) {

                    if (permissionUtils.isStoragePermissionGranted(mContext)) {
                        writeToLogFile(messageType, message);
                    }
                }

                if (messageType.equals(TypeUtils.LogMsgType.ERROR) ||
                    messageType.equals(TypeUtils.LogMsgType.WARNING)) {
                    logSystemMessage(messageType, message);
                }
            }
        } catch(NullPointerException npe) {
            Log.d(TypeUtils.getStrReprLogMsgType(TypeUtils.LogMsgType.ERROR), "Failed to logMessage");
        }
    }

    /**
     * Writes a log message into the console
     */
    public void logSystemMessage(TypeUtils.LogMsgType messageType, String message) {
        try {
            Log.d(TypeUtils.getStrReprLogMsgType(messageType), getLogMessage(messageType, message));

        } catch(NullPointerException npe) {
            Log.d(TypeUtils.getStrReprLogMsgType(TypeUtils.LogMsgType.ERROR), "Failed to logSystemMessage");
        }
    }

    /**
     * Build a log entry with a tag, a timestamp and a message
     */
    private String getLogMessage(TypeUtils.LogMsgType messageType, String message) {
        try {
            DateTimeUtils dateTimeUtils = new DateTimeUtils();

            if (messageType.equals(TypeUtils.LogMsgType.NONE)) {
                return message;
            } else {
                return dateTimeUtils.getNowTimeStamp() + " " + message;
            }

        } catch(NullPointerException npe) {
            return TypeUtils.getStrReprLogMsgType(TypeUtils.LogMsgType.ERROR) + "Failed to getLogMessage";
        }
    }

    /**
     * Writes a log entry into a log file
     */
    private void writeToLogFile(TypeUtils.LogMsgType messageType, String newLogLine) {
        File logFile = new File(LogUtils.LOG_FILENAME);

        try {

            if (!logFile.exists()) {

                if (logFile.createNewFile()) {
                    logSystemMessage(messageType, "Log file was created");
                }
            }

            if (logFile.canWrite()) {
                FileWriter writer = new FileWriter(logFile, true);
                writer.write(getLogMessage(messageType, newLogLine + "\r\n"));
                writer.flush();
                writer.close();
            }

        } catch (IOException e) {
            logSystemMessage(TypeUtils.LogMsgType.ERROR, "Could not writeToLogFile");
            e.getMessage();
        }
    }

}
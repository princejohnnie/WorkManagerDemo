package com.yungjohn.workmanagerdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public static final String KEY_TASK_OUTPUT = "key_task_output";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String desc = data.getString(MainActivity.KEY_TASK_DESC);
        displayNotification("WorkManager", desc);

        Data outputData = new Data.Builder()
                .putString(KEY_TASK_OUTPUT, "Task finished successfully")
                .build();

        return Result.success(outputData);
    }

    private void displayNotification(String text, String desc) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel("channel_1", "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel1);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel_1")
                .setContentTitle(text)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher);

        manager.notify(1, builder.build());
    }
}

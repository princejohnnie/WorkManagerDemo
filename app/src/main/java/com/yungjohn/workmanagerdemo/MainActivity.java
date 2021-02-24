package com.yungjohn.workmanagerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_TASK_DESC = "key_task_desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data.Builder()
                .putString(KEY_TASK_DESC, "Hey, my work data")
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        findViewById(R.id.bt_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
            }
        });

        final TextView tvStatus = findViewById(R.id.tv_status);

        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            Data data = workInfo.getOutputData();
                            String output = data.getString(MyWorker.KEY_TASK_OUTPUT);

                            String status = workInfo.getState().name();
                            tvStatus.append(status + "\n");

                            tvStatus.append(output + "\n");
                        }
                    }
                });
    }
}
package com.example.servicedownloader;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DownloadService downloadService;
    private boolean bound = false;

    private ProgressBar progressBar;
    private TextView tvProgress;
    private TextView tvStatus;
    private Button btnStart;
    private Button btnCancel;
    private EditText etUrl;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            downloadService = binder.getService();
            bound = true;

            downloadService.setProgressListener(new DownloadService.ProgressListener() {
                @Override
                public void onProgress(int progress) {
                    uiHandler.post(() -> {
                        progressBar.setProgress(progress);
                        tvProgress.setText(progress + " %");
                        tvStatus.setText("Downloading...");
                    });
                }

                @Override
                public void onFinished() {
                    uiHandler.post(() -> {
                        tvStatus.setText("Download finished!");
                        tvProgress.setText("100 %");
                        progressBar.setProgress(100);
                        btnStart.setEnabled(true);
                        btnCancel.setEnabled(false);
                    });
                }

                @Override
                public void onCancelled() {
                    uiHandler.post(() -> {
                        // Keep current percentage (do not reset)
                        tvStatus.setText("Paused");
                        btnStart.setEnabled(true);
                        btnCancel.setEnabled(false);
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            downloadService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tvProgress);
        tvStatus = findViewById(R.id.tvStatus);
        btnStart = findViewById(R.id.btnStart);
        btnCancel = findViewById(R.id.btnCancel);
        etUrl = findViewById(R.id.etUrl);

        btnStart.setOnClickListener(v -> {
            if (bound && downloadService != null) {
                downloadService.startDownload();
                tvStatus.setText("Downloading...");
                btnStart.setEnabled(false);
                btnCancel.setEnabled(true);
            }
        });

        btnCancel.setOnClickListener(v -> {
            if (bound && downloadService != null) {
                downloadService.cancelDownload();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, DownloadService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }
}
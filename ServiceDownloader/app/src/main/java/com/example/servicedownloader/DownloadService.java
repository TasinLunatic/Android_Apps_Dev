package com.example.servicedownloader;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadService extends Service {

    public interface ProgressListener {
        void onProgress(int progress);
        void onFinished();
        void onCancelled();
    }

    private final IBinder binder = new LocalBinder();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicInteger progress = new AtomicInteger(0);
    private final AtomicBoolean isDownloading = new AtomicBoolean(false);
    private final AtomicBoolean cancelRequested = new AtomicBoolean(false);
    private ProgressListener listener;

    public class LocalBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setProgressListener(ProgressListener listener) {
        this.listener = listener;
    }

    public void startDownload() {
        if (isDownloading.get()) return;

        isDownloading.set(true);
        cancelRequested.set(false);
        progress.set(0);

        executor.execute(() -> {
            for (int i = 1; i <= 100; i++) {
                if (cancelRequested.get()) {
                    progress.set(0);
                    isDownloading.set(false);
                    if (listener != null) listener.onCancelled();
                    return;
                }
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    progress.set(0);
                    isDownloading.set(false);
                    if (listener != null) listener.onCancelled();
                    return;
                }
                progress.set(i);
                if (listener != null) listener.onProgress(i);
            }
            isDownloading.set(false);
            if (listener != null) listener.onFinished();
        });
    }

    public void cancelDownload() {
        cancelRequested.set(true);
    }

    public int getProgress() {
        return progress.get();
    }

    public boolean isDownloading() {
        return isDownloading.get();
    }

    @Override
    public void onDestroy() {
        cancelRequested.set(true);
        executor.shutdownNow();
        super.onDestroy();
    }
}
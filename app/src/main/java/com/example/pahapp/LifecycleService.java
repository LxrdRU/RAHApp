package com.example.pahapp;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ServiceLifecycleDispatcher;

/**
 * A Service that is also a {@link LifecycleOwner}.
 */
public class LifecycleService extends Service implements LifecycleOwner {
    private final ServiceLifecycleDispatcher mDispatcher = new ServiceLifecycleDispatcher(this);
    @CallSuper
    @Override
    public void onCreate() {
        mDispatcher.onServicePreSuperOnCreate();
        super.onCreate();
    }
    @CallSuper
    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        mDispatcher.onServicePreSuperOnBind();
        return null;
    }
    @SuppressWarnings("deprecation")
    @CallSuper
    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        mDispatcher.onServicePreSuperOnStart();
        super.onStart(intent, startId);
    }
    // this method is added only to annotate it with @CallSuper.
    // In usual service super.onStartCommand is no-op, but in LifecycleService
    // it results in mDispatcher.onServicePreSuperOnStart() call, because
    // super.onStartCommand calls onStart().
    @CallSuper
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @CallSuper
    @Override
    public void onDestroy() {
        mDispatcher.onServicePreSuperOnDestroy();
        super.onDestroy();
    }
    @Override
    @NonNull
    public Lifecycle getLifecycle() {
        return mDispatcher.getLifecycle();
    }
}
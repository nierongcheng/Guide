package com.codi.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Codi on 2015/1/15.
 */
public class HelloService extends Service {

    private static final String TAG = "HelloService";

//    private ServiceHandler mServiceHandler;
//    private Looper mLooper;

    private class ServiceHandler extends Handler {

        private ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            long endTime = System.currentTimeMillis() + 5*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            stopSelf(msg.arg1);

            Log.d(TAG, "stop server");
        }
    }

    @Override
    public void onCreate() {
//        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//
//        mLooper = thread.getLooper();
//        mServiceHandler = new ServiceHandler(mLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "service starting");

        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        Looper mLooper = thread.getLooper();
        ServiceHandler mServiceHandler = new ServiceHandler(mLooper);

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "service destroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}

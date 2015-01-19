package com.codi.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Codi on 2015/1/15.
 */
public class HelloIntentService extends IntentService {

    private static final String TAG = "HelloIntentService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public HelloIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "handle intent service " + Thread.currentThread().getName());

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
        Log.d(TAG, "intent service done");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

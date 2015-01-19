package com.codi.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

/**
 * Created by Codi on 2015/1/19.
 */
public class LocalService extends Service {

    private IBinder mBinder = new LocalBinder();

    private Random mRandom = new Random();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    public int getRandomNumber() {
        return mRandom.nextInt(100);
    }
}

package com.codi.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Codi on 2015/1/19.
 */
public class BindActivity extends Activity {

    private LocalService mService;
    private boolean mIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mIsBound) {
            unbindService(conn);
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((LocalService.LocalBinder) service).getService();
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
        }
    };

    public void callServiceMethod(View view) {
        int num = mService.getRandomNumber();
        Toast.makeText(this, "get number from service: " + num, Toast.LENGTH_SHORT).show();
    }
}

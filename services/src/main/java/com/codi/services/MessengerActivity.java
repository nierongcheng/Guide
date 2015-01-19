package com.codi.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Codi on 2015/1/19.
 */
public class MessengerActivity extends Activity {

    private boolean mIsBound = false;
    private Messenger mService;
    private Messenger mReceiveService;

    private TextView mMessageTV;

    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        mMessageTV = (TextView) findViewById(R.id.message);

        mReceiveService = new Messenger(new InComingHandler());
    }

    public void bindRemoteService(View view) {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    public void unbindRemoteService(View view) {
        if(mIsBound) {
            Message msg = Message.obtain();
            msg.what = MessengerService.MSG_UNREGISTER;
            msg.replyTo = mReceiveService;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            unbindService(mConn);
            mIsBound = false;
            mCount = 0;
            mMessageTV.setText("unbound remote service");
        }
    }

    public void communication(View view) {
        if(mIsBound) {
            Message msg = Message.obtain();
            msg.what = MessengerService.MSG_COMMUNICATION;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    class InComingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_COMMUNICATION:
                    mCount++;
                    mMessageTV.setText("communication numbers: " + mCount);
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }


    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mIsBound = true;

            Message msg = Message.obtain();
            msg.replyTo = mReceiveService;
            msg.what = MessengerService.MSG_REGISTER;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            mMessageTV.setText("bound remote service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mIsBound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unbindRemoteService(null);
    }
}

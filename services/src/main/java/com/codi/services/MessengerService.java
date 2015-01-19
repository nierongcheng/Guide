package com.codi.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Codi on 2015/1/19.
 */
public class MessengerService extends Service {

    public static final int MSG_COMMUNICATION = 0;
    public static final int MSG_REGISTER = 1;
    public static final int MSG_UNREGISTER = 2;

    private Messenger mMessenger = new Messenger(new HelloHandler());
    private ArrayList<Messenger> mClients = new ArrayList<>();

    private class HelloHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_REGISTER:
                    mClients.add(msg.replyTo);
                    break;

                case MSG_UNREGISTER:
                    mClients.remove(msg.replyTo);
                    break;

                case MSG_COMMUNICATION:
                    Toast.makeText(getBaseContext(), "hello from service", Toast.LENGTH_SHORT).show();
                    for (int i = 0, length = mClients.size(); i < length; i++) {
                        try {
                            mClients.get(i).send(Message.obtain(null, MSG_COMMUNICATION, null));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "remote service has stopped", Toast.LENGTH_SHORT).show();
    }
}

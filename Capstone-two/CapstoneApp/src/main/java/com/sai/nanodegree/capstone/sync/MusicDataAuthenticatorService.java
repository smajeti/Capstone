package com.sai.nanodegree.capstone.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MusicDataAuthenticatorService extends Service {

    private MusicDataAuthenticator authenticator;

    public MusicDataAuthenticatorService() {
    }

    @Override
    public void onCreate() {
        authenticator = new MusicDataAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}

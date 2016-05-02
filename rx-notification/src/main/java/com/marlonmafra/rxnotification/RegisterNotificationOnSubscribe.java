package com.marlonmafra.rxnotification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class RegisterNotificationOnSubscribe implements Observable.OnSubscribe<String> {

    private static String TAG = "RxNotification";

    private String gcmRegId;
    private Context context;

    public RegisterNotificationOnSubscribe(@NonNull Context mContext, @NonNull String mGcmRegId) {
        this.context = mContext;
        this.gcmRegId = mGcmRegId;
    }

    @Override
    public void call(final Subscriber<? super String> subscriber) {
        Scheduler.Worker inner = Schedulers.io().createWorker();
        subscriber.add(inner);
        inner.schedule(() -> {
            try {
                String token = InstanceID.getInstance(this.context).getToken(this.gcmRegId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                subscriber.onNext(token);
                subscriber.onCompleted();
                Log.d(TAG, "Notification token:" + token);
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                subscriber.onError(e);
            }
        });
    }
}
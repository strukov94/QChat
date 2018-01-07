package com.strukov.qchat.chat.fragments.messages;

import android.content.SharedPreferences;

import com.strukov.qchat.models.Message;
import com.strukov.qchat.models.User;

import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Matthew on 28.12.2017.
 */

public class MessagesModelImpl implements MessagesModel {
    private Realm mRealm;
    private SharedPreferences mPref;

    public MessagesModelImpl(Realm realm, SharedPreferences sharedPreferences) {
       mRealm = realm;
       mPref = sharedPreferences;
    }

    @Override
    public Flowable<RealmResults<Message>> getLastMessages() {
        User user = mRealm.where(User.class).equalTo("id", getUserPref()).findFirst();
        return user.getMessages().where()
                .sort("date", Sort.DESCENDING).distinctValues("owner").findAllAsync().asFlowable();
    }

    @Override
    public int getUserPref() {
        return mPref.getInt("user_id", 0);
    }

    @Override
    public Realm getRealm() {
        return mRealm;
    }
}

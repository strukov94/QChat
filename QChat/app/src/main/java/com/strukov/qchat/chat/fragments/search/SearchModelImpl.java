package com.strukov.qchat.chat.fragments.search;

import android.content.Context;

import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.models.User;
import com.strukov.qchat.models.Users;
import com.strukov.qchat.utils.NetworkUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Matthew on 24.12.2017.
 */

public class SearchModelImpl implements SeacrhModel {
    private QChatApi mApi;
    private Context mContext;

    public SearchModelImpl(Context context, QChatApi api) {
        mApi = api;
        mContext = context;
    }

    @Override
    public Observable<Users> getUsers() {
        return mApi.getUsers();
    }

    @Override
    public Observable<Boolean> isNetworkAvailable() {
        return NetworkUtils.isNetworkAvailableObservable(mContext);
    }
}

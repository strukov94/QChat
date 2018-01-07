package com.strukov.qchat.chat.fragments.search;

import com.strukov.qchat.models.Users;


import io.reactivex.Observable;

/**
 * Created by Matthew on 24.12.2017.
 */

public interface SeacrhModel {
    Observable<Users> getUsers();

    Observable<Boolean> isNetworkAvailable();
}
package com.strukov.qchat.login;

import android.content.Intent;

/**
 * Created by Matthew on 15.12.2017.
 */

public interface LoginView {
    void startSignInActivity(Intent intent, int requestCode);

    void finishSignInActivity();

    void onNetworkUnavailable();

    void updateMessages(int userId);
}

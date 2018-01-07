package com.strukov.qchat.login;

import android.content.Intent;
import android.view.View;

/**
 * Created by Matthew on 15.12.2017.
 */

public interface LoginPresenter {
    void onCreate(LoginActivity view);

    void onClick(View v);

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onCloseRealm();
}

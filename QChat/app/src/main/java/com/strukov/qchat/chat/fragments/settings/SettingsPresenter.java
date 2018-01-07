package com.strukov.qchat.chat.fragments.settings;

import android.view.View;

import com.strukov.qchat.chat.MainActivity;

/**
 * Created by Matthew on 21.12.2017.
 */

public interface SettingsPresenter {
    void onCreate(SettingsView view);

    void onClick(View v);

    void onDestroy();
}

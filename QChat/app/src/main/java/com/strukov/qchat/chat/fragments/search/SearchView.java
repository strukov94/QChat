package com.strukov.qchat.chat.fragments.search;

import com.strukov.qchat.models.Users;

/**
 * Created by Matthew on 24.12.2017.
 */

public interface SearchView {
    void onNetworkUnavailable();

    void swapAdapter(Users users);
}

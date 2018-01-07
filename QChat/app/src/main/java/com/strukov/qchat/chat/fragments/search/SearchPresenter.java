package com.strukov.qchat.chat.fragments.search;

import io.reactivex.disposables.Disposable;

/**
 * Created by Matthew on 24.12.2017.
 */

public interface SearchPresenter {
    void onCreate(SearchView view);

    Disposable getUsers();

    void onDestroy();
}

package com.strukov.qchat.chat.fragments.search.di;

import android.content.Context;

import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.chat.fragments.search.SearchFragment;
import com.strukov.qchat.chat.fragments.search.SearchModelImpl;
import com.strukov.qchat.chat.fragments.search.SearchPresenterImpl;
import com.strukov.qchat.chat.fragments.search.adapter.SearchAdapter;
import com.strukov.qchat.utils.rx.RxSchedulers;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Matthew on 24.12.2017.
 */

@Module
public class SearchModule {
    @SearchScope
    @Provides
    SearchAdapter providesSearchAdapter(Context context, SearchFragment onClick) {
        return new SearchAdapter(context, onClick);
    }

    @SearchScope
    @Provides
    SearchModelImpl providesSearchModelImpl(Context context, QChatApi api) {
        return new SearchModelImpl(context, api);
    }

    @Named("search")
    @SearchScope
    @Provides
    CompositeDisposable CompositeDisposable() {
        return new CompositeDisposable();
    }

    @SearchScope
    @Provides
    SearchPresenterImpl providesSearchPresenterImpl(SearchModelImpl model, RxSchedulers schedulers, CompositeDisposable disposable) {
        return new SearchPresenterImpl(model, schedulers, disposable);
    }
}

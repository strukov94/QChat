package com.strukov.qchat.chat.fragments.search.di;

import com.strukov.qchat.chat.fragments.search.SearchFragment;

import dagger.Subcomponent;

/**
 * Created by Matthew on 24.12.2017.
 */

@SearchScope
@Subcomponent(modules = SearchModule.class)
public interface SearchComponent {
    void inject(SearchFragment fragment);
}

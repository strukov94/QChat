package com.strukov.qchat.login.di;

import com.strukov.qchat.login.LoginActivity;

import dagger.Subcomponent;

/**
 * Created by Matthew on 15.12.2017.
 */

@LoginScope
@Subcomponent(modules = LoginModule.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}

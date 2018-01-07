package com.strukov.qchat.login.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Matthew on 15.12.2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginScope {
}

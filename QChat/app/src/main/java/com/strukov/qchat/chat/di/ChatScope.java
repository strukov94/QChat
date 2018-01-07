package com.strukov.qchat.chat.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Matthew on 14.12.2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ChatScope {
}

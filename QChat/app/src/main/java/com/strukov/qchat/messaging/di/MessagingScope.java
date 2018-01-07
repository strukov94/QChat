package com.strukov.qchat.messaging.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Matthew on 26.12.2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface MessagingScope {
}

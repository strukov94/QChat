package com.strukov.qchat.utils.rx;


import io.reactivex.Scheduler;

/**
 * Created by Matthew on 14.12.2017.
 */

public interface RxSchedulers {
    Scheduler runOnBackground();

    Scheduler io();

    Scheduler compute();

    Scheduler androidThread();

    Scheduler internet();
}

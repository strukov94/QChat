package com.strukov.qchat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Matthew on 28.12.2017.
 */

public class ConvertDate {
    public static String getDate(long timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm, EEE");
        Date date = new Date(timeStamp * 1000L);
        return format.format(date);
    }
}

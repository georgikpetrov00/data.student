package com.grandp.data.log_helper;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ThreadIdConverter extends ClassicConverter {
    @Override
    public String convert(final ILoggingEvent e) {
        Thread currentThread = Thread.currentThread();
        return String.valueOf( "T@" + currentThread.hashCode());
    }
}
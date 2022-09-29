package com.payneteasy.proxy.log;

public class LoggerFactory {
    public static ILogger getLogger(Class<?> aClass) {
        return new LoggerImpl(aClass.getName());
    }
}

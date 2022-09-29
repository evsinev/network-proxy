package com.payneteasy.proxy.log;

public interface ILoggerContext {

    ILoggerContext keyValue(String aName, String aValue);

    ILoggerContext keyValue(String aName, Object aValue);

    ILoggerContext keyValue(String aName, int aValue);

    void exception(Exception e);
}

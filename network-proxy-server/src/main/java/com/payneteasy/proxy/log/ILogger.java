package com.payneteasy.proxy.log;

public interface ILogger {

    void info(String aName, ILoggerProducer aProducer);

    void error(String aName, ILoggerProducer aProducer);

    void warn(String aName, ILoggerProducer aProducer);

    void debug(String aName, ILoggerProducer aProducer);

    ILogger mdc(String aName, String aValue);

    void clearMdc();

}

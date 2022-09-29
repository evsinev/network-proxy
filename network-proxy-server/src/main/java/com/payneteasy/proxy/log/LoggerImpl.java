package com.payneteasy.proxy.log;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LoggerImpl implements ILogger {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss.SSS z");
    private static final ThreadLocal<Map<String, String>> MDC = ThreadLocal.withInitial(() -> new HashMap<>());

    private final String name;

    public LoggerImpl(String aName) {
        name = aName;
    }

    @Override
    public void info(String aName, ILoggerProducer aProducer) {
        log("INFO", aName, aProducer);
    }

    @Override
    public void error(String aName, ILoggerProducer aProducer) {
        log("ERROR", aName, aProducer);
    }

    @Override
    public void warn(String aName, ILoggerProducer aProducer) {
        log("WARN", aName, aProducer);
    }

    @Override
    public void debug(String aName, ILoggerProducer aProducer) {
        log("DEBUG", aName, aProducer);
    }

    @Override
    public ILogger mdc(String aName, String aValue) {
        MDC.get().put(aName, aValue);
        return this;
    }

    @Override
    public void clearMdc() {
        MDC.remove();
    }

    private void log(String aLevel, String aName, ILoggerProducer aProducer) {
        StringBuilder sb = new StringBuilder();
        sb.append("proxy ");
        sb.append(DATE_FORMATTER.format(ZonedDateTime.now()));
        sb.append(' ');
        sb.append(aLevel);
        sb.append(' ');
        sb.append(Thread.currentThread().getName());
        sb.append(' ');
        sb.append(aName);
        sb.append(' ');
        sb.append(MDC.get());

        LoggerContextImpl context = new LoggerContextImpl(sb);
        aProducer.log(context);

        System.err.println(sb);
        System.err.flush();

    }

}

package com.payneteasy.proxy.log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LoggerContextImpl implements ILoggerContext {

    private final StringBuilder sb;

    public LoggerContextImpl(StringBuilder aSb) {
        sb = aSb;
    }

    @Override
    public ILoggerContext keyValue(String aName, String aValue) {
        sb.append(' ').append(aName).append('=').append(aValue);
        return this;
    }

    @Override
    public ILoggerContext keyValue(String aName, Object aValue) {
        return keyValue(aName, aValue != null ? aValue.toString() : "<null>");
    }

    @Override
    public ILoggerContext keyValue(String aName, int aValue) {
        return keyValue(aName, String.valueOf(aValue));
    }

    @Override
    public void exception(Exception e) {
        sb.append(' ');
        sb.append(e.getMessage());
        sb.append('\n');
        StringWriter out = new StringWriter();
        PrintWriter  writer   = new PrintWriter(out);
        e.printStackTrace(writer);
        writer.flush();
        sb.append(out);
    }

}

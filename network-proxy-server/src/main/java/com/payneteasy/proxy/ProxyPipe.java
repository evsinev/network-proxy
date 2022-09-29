package com.payneteasy.proxy;


import com.payneteasy.proxy.log.ILogger;
import com.payneteasy.proxy.log.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ProxyPipe implements Runnable {

    private static final ILogger LOG = LoggerFactory.getLogger(ProxyPipe.class);

    private final Socket               source;
    private final Socket               destination;
    private final String               inName;
    private final String               outName;

    public ProxyPipe(String aInName, String aOutName, Socket aSource, Socket aDestination) {
        source = aSource;
        destination = aDestination;
        inName = aInName;
        outName = aOutName;
    }

    @Override
    public void run() {
        LOG.mdc("in", inName)
                .mdc("out", outName);
        try {

            byte[] buf = new byte[10_240];
            int count;
            try(InputStream in = source.getInputStream()) {
                try(OutputStream out = destination.getOutputStream()) {
                    while( (count = in.read(buf)) >=0 ) {
                        final int length = count;
//                        LOG.info("Received bytes", aContext -> aContext.keyValue("length", length));
                        out.write(buf, 0, count);
                        out.flush();
//                        LOG.info("Sent bytes", aContext -> aContext.keyValue("length", length));
                    }
                }
            }
        }  catch (Exception e) {
            if("Socket closed".equals(e.getMessage())) {
                LOG.warn("Socket closed", aContext -> {});
            } else {
                LOG.error("Error sending or receiving bytes", aContext -> aContext.exception(e));
            }
            closeSource();
        } finally {
            LOG.clearMdc();
        }
    }

    public void closeSource() {
        try {
            source.close();
        } catch (IOException e) {
            LOG.error(inName , (context) -> context.exception(e));
        }
    }

    
}

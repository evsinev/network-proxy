package com.payneteasy.proxy;

import com.payneteasy.proxy.log.ILogger;
import com.payneteasy.proxy.log.LoggerFactory;
import com.payneteasy.proxy.util.Arguments;
import com.payneteasy.proxy.util.Networks;

import java.io.IOException;

public class ProxyApplication {

    private static final ILogger LOG = LoggerFactory.getLogger(ProxyApplication.class);

    public static void main(String[] args) throws IOException {
        Arguments arguments     = new Arguments(args);
        String listenAddress = auto(arguments.getString("--listen-address", "-la"));
        int    listenPort    = arguments.getInt   ("--listen-port"   , "-lp");
        String targetAddress = arguments.getString("--target-address", "-ta");
        int    targetPort    = arguments.getInt   ("--target-port"   , "-tp");

        LOG.info("Startup parameters", aContext -> aContext
                .keyValue("listen-address", listenAddress)
                .keyValue("listen-port"   , listenPort)
                .keyValue("target-address", targetAddress)
                .keyValue("target-port"   , targetPort)
        );

        ProxyServer proxyServer = new ProxyServer(listenAddress, listenPort);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Closing server...", aContext -> {});
            proxyServer.close();
        }));
        proxyServer.acceptSocketAndWait(targetAddress, targetPort);

    }

    private static String auto(String aIpAddress) {
        if("first-ipv4".equals(aIpAddress)) {
            return Networks.getFirstIpInet4Addresses();
        }
        return aIpAddress;
    }
}

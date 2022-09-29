package com.payneteasy.proxy;


import com.payneteasy.proxy.log.ILogger;
import com.payneteasy.proxy.log.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyServer {

    private static final ILogger LOG = LoggerFactory.getLogger(ProxyServer.class);

    private final ServerSocket    serverSocket;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ProxyServer(String aListenAddress, int aListenPort) throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(aListenAddress, aListenPort));
    }

    public void acceptSocketAndWait(String aTargetAddress, int aTargetPort) {
        LOG.info("Start listening", aContext -> aContext.keyValue("serverSocket", serverSocket));

        while (!Thread.currentThread().isInterrupted()) {
            try {
                LOG.info("Waiting for incoming connection", aContext -> {
                });
                final Socket clientSocket = serverSocket.accept();

                LOG.info("Connected from client", aContext -> aContext
                        .keyValue("remoteSocketAddress", clientSocket.getRemoteSocketAddress())
                );

                LOG.info("Connecting to target...", aContext -> aContext
                        .keyValue("targetAddress", aTargetAddress)
                        .keyValue("targetPort", aTargetPort)
                );

                LOG.mdc("acceptedSocket", clientSocket.toString());
                try {
                    Socket targetSocket = new Socket(aTargetAddress, aTargetPort);
                    LOG.info("Connected to target", aContext -> aContext.keyValue("targetSocket", targetSocket.getRemoteSocketAddress()));

                    ProxyPipe clientPipe = new ProxyPipe(
                            socketName("client", clientSocket)
                            , socketName("target", targetSocket)
                            , clientSocket
                            , targetSocket
                    );

                    ProxyPipe serverPipe = new ProxyPipe(
                            socketName("target", targetSocket)
                            , socketName("client", clientSocket)
                            , targetSocket
                            , clientSocket
                    );

                    executor.execute(serverPipe);
                    executor.execute(clientPipe);
                } catch (Exception e) {
                    LOG.error("Cannot connect to target", aContext -> aContext.exception(e));
                    closeAcceptedSocket(clientSocket);
                    LOG.clearMdc();
                }

            } catch (Exception e) {
                if (Thread.currentThread().isInterrupted()) {
                    LOG.error("Exiting from the listening server socket ...", aContext -> {
                    });
                    break;
                }
                LOG.error("Cannot deal with socket error", aContext -> aContext.exception(e));
            }
        }
    }

    private String socketName(String aName, Socket aSocket) {
        return aName + aSocket.getInetAddress() + ":" + aSocket.getPort();
    }

    private void closeAcceptedSocket(Socket clientSocket) {
        try {
            clientSocket.close();
        } catch (Exception e) {
            LOG.error("Cannot close accepted socket", aContext -> aContext
                    .keyValue("socket", clientSocket)
                    .exception(e));
        }
    }

    public void close() {
        try {
            serverSocket.close();
            LOG.info("Socket closed", aContext -> {});
        } catch (Exception e) {
            LOG.error("Cannot close server socket", aContext -> aContext.exception(e));
        }
    }
}

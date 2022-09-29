package com.payneteasy.proxy.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import static java.net.NetworkInterface.getNetworkInterfaces;
import static java.util.Collections.list;

public class Networks {

    private Networks() {
    }

    public static List<String> listIpInet4Addresses() {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = getNetworkInterfaces();
        } catch (SocketException e) {
            throw new IllegalStateException("Cannot list network interfaces", e);
        }

        return list(networkInterfaces).stream()
                .flatMap(iface -> list(iface.getInetAddresses()).stream())
                .filter(address -> !address.isLoopbackAddress())
                .filter(address -> address instanceof Inet4Address)
                .map(InetAddress::getHostAddress)
                .collect(Collectors.toList());
    }

    public static String getFirstIpInet4Addresses() {
        return listIpInet4Addresses().stream()
                .findFirst().orElseThrow(() -> new IllegalStateException("Cannot find any interface"));
    }
}
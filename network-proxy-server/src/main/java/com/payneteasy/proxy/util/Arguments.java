package com.payneteasy.proxy.util;

import java.util.Arrays;

public class Arguments {

    private final String[] args;

    public Arguments(String[] args) {
        this.args = args;
    }

    public String getString(String ... aNames) {
        for(int i=0; i < args.length; i++) {
            String arg = args[i];
            for (String name : aNames) {
                if(arg.equals(name)) {
                    return args[i + 1];
                }
            }
        }
        throw new IllegalStateException("Argument " + Arrays.asList(aNames) + " not found");
    }

    public int getInt(String ... aNames) {
        return Integer.parseInt(getString(aNames));
    }
}

package com.risenservers.internal.managers.managed;

import com.risenservers.internal.RisenObfuscation;
import com.risenservers.internal.managers.ObfuscationManager;

import java.io.File;

@SuppressWarnings("unused")
public abstract class ObfuscationLayer {

    public ObfuscationLayer(final ObfuscationManager superManager) {}

    public static int dayzHash(String str) {
        int hash = 0, c = 1, length = str.length();
        for (int i = 0; i < length; i++) {
            hash += str.charAt(length - 1 - i) * c;
            c *= 37;
        }
        return hash;
    }

    public void obfLayerLog(String str) {
        System.out.println("[" +
            this.getClass().getSimpleName().replace("Obfuscation", " Obfuscation Layer] ") +
            str);
    }

    @SuppressWarnings("unused")
    public void mainLog(String str) {
        RisenObfuscation.log(str);
    }

    //TODO: make this not retarded
    public abstract File obfuscateFile(File file, String output);
}
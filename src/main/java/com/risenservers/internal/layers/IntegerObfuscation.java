package com.risenservers.internal.layers;

import com.risenservers.internal.managers.ObfuscationManager;
import com.risenservers.internal.managers.managed.ObfuscationLayer;

import java.io.File;

@SuppressWarnings("unused")
public class IntegerObfuscation extends ObfuscationLayer {
    public IntegerObfuscation(ObfuscationManager superManager) {
        super(superManager);

    }

    /**
     * @param file Input file to obfuscate, usually going to be a
     *             ".c" file.
     * @param output Output folder. Path should point to a directory
     *               not a file. TODO: NOT USED IN THIS CLASS
     * @return True if obfuscation succeeded, false if the process
     *         failed
     */
    @Override
    public File obfuscateFile(File file, String output) {
        return null;
    }
}
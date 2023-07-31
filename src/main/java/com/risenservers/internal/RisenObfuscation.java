package com.risenservers.internal;

import com.risenservers.internal.managers.ObfuscationManager;
import com.risenservers.internal.utils.constants.Definitions;
import com.risenservers.internal.utils.constants.Errors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RisenObfuscation {
    @SuppressWarnings("unused")
    public static ObfuscationManager obfuscationManager;
    public static final String MAIN_PREFIX = "[Risen Obfuscation] ";
    public static Path pathToObfuscate;

    /**
     * @param args Java arguments
     * @throws Exception Handle all exceptions poorly
     */
    public static void main(String[] args) throws Exception {
        log("(" + Definitions.PROGRAM_VERSION + ") Starting up...");
        // Checks if arguments are set
        if (args.length == 0) killMyself(Errors.ARGUMENTS_ARE_NULL);
        else pathToObfuscate = Paths.get(args[0]);

        // Checks if specified path exists
        if (!Files.exists(pathToObfuscate)) killMyself(Errors.ARGUMENT_PATH_DOES_NOT_EXIST);
        else {
            //TODO: This may fail to obfuscate when passing a file as an argument.
            if (Files.isDirectory(pathToObfuscate)) obfuscationManager = new ObfuscationManager(pathToObfuscate.toFile());
        }
    }

    public static void killMyself(String why) {
        log(why);
        System.out.println(Definitions.SUPPORT_TEXT);
        System.exit(1);
    }

    public static void log(String str) {
        System.out.println(MAIN_PREFIX + str);
    }
}
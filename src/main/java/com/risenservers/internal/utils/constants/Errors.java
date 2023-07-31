package com.risenservers.internal.utils.constants;

import java.io.File;

public class Errors {
    //GENERIC ERRORS
    public static String GENERIC_FILE_READ(File cause) {
        return "Failed to read contents of file " + cause.getPath() + " \n This could be due to a number of reasons. \n" + "Try verifying that the file exists and is readable by the current Windows user. \n";
    }

    public static final String OBFUSCATION_FAILURE = "Error 0x0003: Failed to obfuscate a file. Please make this an issue on " + Definitions.SUPPORT_URL;

    public static final String ARGUMENTS_ARE_NULL = "Error 0x0001: No arguments are set, Try using a path to a folder as an argument. \n\rExample:\tProgram Arguments = \"C:\\Users\\admin\\obf\"";
    public static final String ARGUMENT_PATH_DOES_NOT_EXIST = "Error 0x0002: Could not find the folder specified in the program arguments. \n\r Be sure to verify that the folder used in your program arguments actually exists.";
}
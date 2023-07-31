package com.risenservers.internal.utils;

import com.risenservers.internal.RisenObfuscation;
import com.risenservers.internal.utils.constants.Errors;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

@SuppressWarnings("ALL")
public class FileUtilities {

    public static String[] fileToLinesArray(File inputFile) {
        return Objects.requireNonNull(fileToLines(inputFile)).split("\\n");
    }

    public static List < String > fileToLinesList(File inputFile) {
        return Arrays.asList(Objects.requireNonNull(fileToLines(inputFile)).split("\\n"));
    }

    public static String fileToLines(File inputFile) {
        try {
            StringBuilder lineBuilder = new StringBuilder();
            for (String line: Files.readAllLines(inputFile.toPath())) {
                lineBuilder.append(line).append("\n");
            }

            return lineBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            RisenObfuscation.killMyself(Errors.GENERIC_FILE_READ(inputFile));
        }
        return null;
    }

    public static String escapeQuotes(String input) {
        return input.replaceAll("\"", "\\\\\"");
    }

    public static void removeEmptyLines(File file) {
        StringBuilder write = new StringBuilder();
        for (String line: fileToLinesList(file)) {
            if (line != "" && line != "\n") write.append(line).append("\n");
        }
        writeStringToFile(file, write.toString());
    }

    public static void writeStringToFile(File writeTo, String str) {
        try {
            FileOutputStream fileOut = new FileOutputStream(writeTo);
            fileOut.write(str.getBytes(StandardCharsets.UTF_8));
            fileOut.close();
        } catch (Exception e) {
            RisenObfuscation.killMyself(Errors.GENERIC_FILE_READ(writeTo));
        }

    }

    public static boolean fileContainsComments(String fileToLinesResponse) {
        List < String > splitAtComments = Arrays.asList(fileToLinesResponse.split("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)"));
        return splitAtComments.size() > 1;
    }

    //TODO: This may fuck up when ran on a file with strings containing comment syntax.
    public static void removeCommentsFromFile(File file) {
        String fileText = fileToLines(file);
        Objects.requireNonNull(fileText).replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        writeStringToFile(file, fileText);
    }

    public static String removeCommentsFromString(String str) {
        return (str).replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
    }

    //TODO: This may fuck up when ran on a file with strings containing comment syntax.
    public static void removeCommentsFromFile(File input, File output) {
        String fileText = fileToLines(input);
        Objects.requireNonNull(fileText).replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        writeStringToFile(output, fileText);
    }

    public static void addLineToFile(File input, File output, String line) {
        StringBuilder fileText = new StringBuilder(Objects.requireNonNull(fileToLines(input)));
        fileText.append(fileText.toString().endsWith("\\n") ? line : "\n" + line);
        writeStringToFile(output, fileText.toString());
    }

    public static void addLineToFile(File input, String line) {
        StringBuilder fileText = new StringBuilder(Objects.requireNonNull(fileToLines(input)));
        fileText.append(fileText.toString().endsWith("\\n") ? line : "\n" + line);
        writeStringToFile(input, fileText.toString());
    }

    //TODO: Recursive file search, contain file structure in output
    public static List < File > listOfFiles(File dirPath) {
        if (dirPath == null) return Collections.emptyList();

        List < File > returnList = new LinkedList < > (Arrays.asList(Objects.requireNonNull(dirPath.listFiles())));
        for (File file: returnList) {
            // Remove directories from list
            if (file.isDirectory()) returnList.remove(file);

            RisenObfuscation.log("Found file: " + file.getPath());

            if (file.getPath().contains("\\obf")) {
                returnList.remove(file);
                RisenObfuscation.log("Removed obfuscated file from list: " + file.getPath());
            }
        }
        return returnList;

    }
}
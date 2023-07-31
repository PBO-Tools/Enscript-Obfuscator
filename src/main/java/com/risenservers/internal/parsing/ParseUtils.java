package com.risenservers.internal.parsing;

import com.risenservers.internal.RisenObfuscation;
import com.risenservers.internal.parsing.dayz.EnforceData;
import com.risenservers.internal.utils.FileUtilities;
import com.risenservers.internal.utils.constants.Definitions;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ParseUtils {

    public static void parseFiles(List < File > fileList) {
        for (File file: fileList) {
            if (Definitions.C_FILES_ONLY && !(file.getName().endsWith(".c"))) {
                System.out.println(RisenObfuscation.MAIN_PREFIX + "Ignoring File: " + file.getName());
            } else {
                System.out.println(RisenObfuscation.MAIN_PREFIX + "Starting job: " + file.getName());
                EnforceData parsed = parseFile(file);
            }
        }
    }

    /**
     * @param file parse a single file.
     */
    public static EnforceData parseFile(File file) {
        EnforceData returnObj = new EnforceData();
        for (String line: FileUtilities.fileToLinesList(file)) {
            {
                String strPattern = "\"[^\"]*\"";

                Pattern pattern = Pattern.compile(strPattern);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    returnObj.addStringToList(matcher.group());
                }
            } {
                String strPattern = "([\\\\+-]?\\\\d+)([eE][\\\\+-]?\\\\d+)?";

                Pattern pattern = Pattern.compile(strPattern);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    if (!Character.isLetter(line.charAt(line.indexOf(matcher.group())))) {
                        returnObj.addIntegerToList(Integer.parseInt(matcher.group()));
                    }
                }
            }
        }
        return returnObj;
    }
}
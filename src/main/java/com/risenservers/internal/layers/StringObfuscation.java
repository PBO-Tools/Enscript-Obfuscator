package com.risenservers.internal.layers;

import com.risenservers.internal.managers.ObfuscationManager;
import com.risenservers.internal.managers.managed.ObfuscationLayer;
import com.risenservers.internal.parsing.ParseUtils;
import com.risenservers.internal.parsing.dayz.EnforceData;
import com.risenservers.internal.utils.FileUtilities;
import com.risenservers.internal.utils.constants.Definitions;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.risenservers.internal.utils.FileUtilities.escapeQuotes;

@SuppressWarnings("ALL")
public class StringObfuscation extends ObfuscationLayer {
    public static final String tabOffPage = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
    public static final String decodeFunctionName = "decodeRisen";
    private static final String CIPHER_CHARS = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    public final String enforce_decrypt = getEnforce_decrypt(decodeFunctionName);

    private final String defaultSeed = "risen";

    public StringObfuscation(ObfuscationManager superManager) {
        super(superManager);
    }

    /**
     * @param file Input file to obfuscate, usually going to be a
     *             ".c" file.
     * @param output Output folder. Path should point to a directory
     *               not a file.
     * @return True if obfuscation succeeded, false if the process
     *         failed
     */
    @Override
    public File obfuscateFile(File file, String output) {
        try {
            File outputFile = new File(new File(file.getParentFile(), output), file.getName());
            if (!outputFile.exists()) outputFile.createNewFile();
            if (file.getPath().contains("obf")) return null;

            //TODO-MAYBE: change to ParseUtils.parseStrings()
            EnforceData parsedData = ParseUtils.parseFile(file);

            StringBuilder obfuscatedLines = new StringBuilder();

            //int lineNumber = 1;
            for (String line: FileUtilities.fileToLinesList(file)) {
                if (!(line.endsWith(Definitions.DONT_OBFUSCATE))) line = FileUtilities.removeCommentsFromString(line);
                for (String originalString: parsedData.getStringList()) {
                    if (line.contains(originalString) && !(line.contains("/*") || line.contains("*/"))) {
                        if (line.endsWith(Definitions.DONT_OBFUSCATE)) {
                            line = line.substring(0, line.length() - 9);
                            continue;
                        }
                        String scrambledString = this.scrambleWithDefaultSeed(originalString);
                        line = line.replaceAll(originalString, decodeFunctionName + "(\"" + scrambledString + "\", \"" + this.defaultSeed + "\".Hash())");
                    }
                }

                obfuscatedLines.append(line).append("\r\n");
                //lineNumber++;
            }

            obfuscatedLines.append(this.enforce_decrypt);

            FileOutputStream fileOut = new FileOutputStream(outputFile);
            fileOut.write(obfuscatedLines.toString().getBytes(StandardCharsets.UTF_8));
            fileOut.close();
            return outputFile;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public File obfuscateFileWithSig(File file, String output, String funName) {
        try {
            funName = funName.replaceAll("[0-9]", "");
            new File(file.getParentFile(), output).mkdirs();

            //FIXME: Ew lol
            File outputFile = new File(new File(file.getParentFile(), output), file.getName());

            if (!outputFile.exists()) outputFile.createNewFile();
            if (file.getPath().contains("obf")) return null;

            EnforceData parsedData = ParseUtils.parseFile(file);
            StringBuilder obfuscatedLines = new StringBuilder();

            //int lineNumber = 1;
            for (String line: FileUtilities.fileToLinesList(file)) {
                // Remove comments from Fil, deont put dontobf in comments FIXME: ISSUE #3 RESOLVED MAYBE
                if (!(line.endsWith(Definitions.DONT_OBFUSCATE))) line = FileUtilities.removeCommentsFromString(line);

                for (String originalString: parsedData.getStringList()) {
                    if (line.contains(originalString)) {
                        if (line.endsWith(Definitions.DONT_OBFUSCATE)) {
                            line = line.substring(0, line.length() - 9);
                            continue;
                        }

                        //FIXME: Really weird bug when parsing a line containing "( "  While obfuscating
                        //java.util.regex.PatternSyntaxException: Unclosed group near index 4 "( "
                        if (originalString.contains("(") || originalString.contains(")")) {
                            line.replace("\" )\"", "\")\"");
                            line.replace("\" (\"", "\"(\"");
                            continue;
                        }

                        String scrambledString = escapeQuotes(this.scrambleWithDefaultSeed(originalString));
                        line = line.replaceAll(originalString, funName + "(\"" + scrambledString.replaceAll("\"", "\\\"") + "\", \"" + this.defaultSeed + "\".Hash())");
                    }
                }

                obfuscatedLines.append(line).append("\n");
                //lineNumber++;
            }

            obfuscatedLines.append(this.getEnforce_decrypt(funName));

            FileOutputStream fileOut = new FileOutputStream(outputFile);
            fileOut.write(obfuscatedLines.toString().getBytes(StandardCharsets.UTF_8));
            fileOut.close();
            return outputFile;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List < String > encryptTheseStrings(List < String > stringList) {
        List < String > returnList = new ArrayList < > ();
        for (String in: stringList) {
            returnList.add(scramble(in, dayzHash(this.defaultSeed)));
        }

        return returnList;
    }

    public List < String > encryptTheseStrings(List < String > stringList, int seed) {
        List < String > returnList = new ArrayList < > ();
        for (String in: stringList) {
            returnList.add(scramble(in, seed));
        }
        return returnList;
    }

    public HashMap < String, String > encryptTheseStringsShowOriginal(List < String > stringList) {
        HashMap < String, String > returnMap = new LinkedHashMap < > ();
        List < String > encList = new ArrayList < > ();
        for (String in: stringList) {
            String scrambled = scramble(in.replace("\"", ""), dayzHash(this.defaultSeed));
            encList.add("\"" + scrambled + "\"");
        }

        for (int i = 0; i < stringList.size(); i++) {
            returnMap.put(stringList.get(i), encList.get(i));
        }

        return returnMap;
    }

    public List < String > decryptTheseStrings(List < String > stringList) {
        List < String > returnList = new ArrayList < > ();
        for (String in: stringList) {
            returnList.add(unscramble(in, dayzHash(this.defaultSeed)));
        }

        return returnList;
    }

    public List < String > decryptTheseStrings(List < String > stringList, int seed) {
        List < String > returnList = new ArrayList < > ();
        for (String in: stringList) {
            returnList.add(unscramble(in, seed));
        }
        return returnList;
    }

    public String scrambleWithDefaultSeed(String input) {
        return scramble(input, dayzHash(this.defaultSeed));
    }

    public String unscrambleWithDefaultSeed(String input) {
        return unscramble(input, dayzHash(this.defaultSeed));
    }

    public String scramble(String input, int seed) {
        StringBuilder stringBuilder = new StringBuilder();
        seed = seed % 10;

        try {
            for (char c: input.substring(1, input.length() - 1).toCharArray()) {
                int charInCipher = CIPHER_CHARS.indexOf(c);
                char newChar = CIPHER_CHARS.charAt(CIPHER_CHARS.indexOf(c) + seed);
                if (newChar == '"' || newChar == '\'') {
                    stringBuilder.append("\\").append(newChar);
                } else stringBuilder.append(newChar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String unscramble(String input, int seed) {
        StringBuilder stringBuilder = new StringBuilder();
        seed = seed % 10;
        System.out.println(seed);

        for (char c: input.toCharArray()) {
            int charInCipher = CIPHER_CHARS.indexOf(c);
            char newChar = CIPHER_CHARS.charAt(CIPHER_CHARS.indexOf(c) - seed);
            stringBuilder.append(newChar);
        }

        return stringBuilder.toString();
    }

    public String getEnforce_decrypt(String funName) {
        return tabOffPage + "string " + funName + "(string text, int seed) { string returnStr = \"\"; int offset = seed % 10; string chars = \" !\\\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ !\\\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ !\\\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\"; for(int charPos = 0; charPos < text.Length(); charPos++) { int newChar = chars.IndexOf(text[charPos]) - offset; returnStr = returnStr + chars[newChar] + \"\"; } return returnStr; }";
    }
}
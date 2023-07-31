package com.risenservers.internal.layers;

import com.risenservers.internal.RisenObfuscation;
import com.risenservers.internal.managers.ObfuscationManager;
import com.risenservers.internal.managers.managed.ObfuscationLayer;
import com.risenservers.internal.utils.FileUtilities;
import com.risenservers.internal.utils.RandomString;
import com.risenservers.internal.utils.constants.Errors;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@SuppressWarnings("unused")
public class CommentObfuscation extends ObfuscationLayer {

    public CommentObfuscation(ObfuscationManager superManager) {
        super(superManager);
    }

    @Override
    public File obfuscateFile(File file, String output) {
        try {
            if (!file.isFile() || !file.canRead()) RisenObfuscation.killMyself(Errors.GENERIC_FILE_READ(file));

            String fileContents = FileUtilities.fileToLines(file);
            RandomString randomString = new RandomString(10);
            StringBuilder obfuscatedLines = new StringBuilder();

            //TODO: Wow this really sucks
            List < String > fuckItUp = Arrays.asList(fileContents.replace("    ", "").replace(" \"", "\"").replace("\" ", "\"").split(" "));

            StringBuilder commented = new StringBuilder();
            for (String segment: fuckItUp) {
                obfuscatedLines.append(segment).append(" /*").append(randomString.nextString()).append("*/ ");
            }

            //noinspection Annotator
            FileUtilities.writeStringToFile(file, obfuscatedLines.toString().replaceAll("GetGame().RequestExit", StringObfuscation.tabOffPage + "GetGame().RequestExit("));
            FileUtilities.removeEmptyLines(file);
            return file;
        } catch (Exception e) {
            RisenObfuscation.killMyself(Errors.OBFUSCATION_FAILURE);
        }
        return null;
    }
}
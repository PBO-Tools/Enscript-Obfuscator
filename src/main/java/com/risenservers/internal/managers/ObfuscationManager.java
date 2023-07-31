package com.risenservers.internal.managers;

import com.risenservers.internal.layers.CommentObfuscation;
import com.risenservers.internal.layers.StringObfuscation;
import com.risenservers.internal.managers.managed.ObfuscationLayer;
import com.risenservers.internal.utils.RandomString;
import com.risenservers.internal.utils.constants.Definitions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.risenservers.internal.utils.FileUtilities.listOfFiles;

@SuppressWarnings("unused")
public final class ObfuscationManager {
    private List < ObfuscationLayer > obfLayers = new ArrayList < > ();
    private ObfuscationLayer stringLayer, commentLayer;

    @Deprecated
    public ObfuscationManager() throws Exception {
        this.initiateLayers();
    }

    public ObfuscationManager(File inputFolder) throws Exception {
        List < File > fileList = listOfFiles(inputFolder);

        this.initiateLayers();

        for (File f: fileList) {
            //TODO: Ignore this, it's been a long day
            RandomString randomString = new RandomString(4);
            if (!f.getPath().contains("obf")) {
                String rand = randomString.nextString();

                //Why?: Functions can NOT contain numbers, Surprisingly they CAN contain quotes. ISSUE FIX
                rand.replaceAll("[0-9]", "y");

                rand = "\"LMFAO" + rand + "LMFAO\"";

                File obf1 = ((StringObfuscation) stringLayer).obfuscateFileWithSig(f, "obf", rand);
                commentLayer.obfuscateFile(obf1, null);
            }
        }
    }

    /**
     * Load obfuscation layers
     */
    private void initiateLayers() throws Exception {
        stringLayer = new StringObfuscation(this);
        obfLayers.add(stringLayer);
        obfManagerLog(stringLayer.getClass().getSimpleName().replace("Obfuscation", " obfuscation layer has been initialized!"));

        arbitraryWait();

        commentLayer = new CommentObfuscation(this);
        obfLayers.add(commentLayer);
        obfManagerLog(commentLayer.getClass().getSimpleName().replace("Obfuscation", " obfuscation layer has been initialized!"));

        obfLayers = Collections.unmodifiableList(obfLayers);
    }

    private void obfManagerLog(String str) {
        System.out.println("[Obfuscation Manager] " + str);
    }

    private static void arbitraryWait() throws Exception {
        if (Definitions.DISABLE_WAIT) return;
        Thread.sleep(1000);
    }

}
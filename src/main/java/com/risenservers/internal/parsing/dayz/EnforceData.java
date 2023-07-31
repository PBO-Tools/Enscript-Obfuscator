package com.risenservers.internal.parsing.dayz;

import com.risenservers.internal.managers.managed.ObfuscationLayer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({
    "unused",
    "MismatchedQueryAndUpdateOfCollection"
})
public class EnforceData {
    private int amountOfStrings, amountOfIntegers;
    private List < String > stringList = new ArrayList < > ();
    private List < Integer > integerList = new ArrayList < > ();
    private final List < String > stringsToHash = new ArrayList < > ();
    private final List < Integer > hashedStrings = new ArrayList < > ();

    public EnforceData(List < String > strings, List < Integer > ints) {
        this.stringList = strings;
        this.integerList = ints;

    }

    public void hashStrings() {
        for (String str: stringsToHash) {
            hashedStrings.add(ObfuscationLayer.dayzHash(str));
        }
    }

    public EnforceData() {}

    public int getAmountOfIntegers() {
        return amountOfIntegers;
    }

    public int getAmountOfStrings() {
        return amountOfStrings;
    }

    public List < Integer > getIntegerList() {
        return integerList;
    }

    public List < String > getStringList() {
        return stringList;
    }

    public List < String > getStringsToHash() {
        return stringsToHash;
    }

    public void addIntegerToList(int integer) {
        this.integerList.add(integer);
    }

    public void addStringToList(String str) {
        this.stringList.add(str);
    }

    public void addStringToHash(String str) {
        this.stringsToHash.add(str);
    }

}
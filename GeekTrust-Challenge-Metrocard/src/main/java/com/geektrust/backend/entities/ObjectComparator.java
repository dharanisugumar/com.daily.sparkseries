package com.geektrust.backend.entities;

import java.util.Comparator;

public class ObjectComparator implements Comparator<String> {

    // Overriding compare()method of Comparator

    public int compare(String s1, String s2) {
        return s2.compareTo(s1);
    }
}
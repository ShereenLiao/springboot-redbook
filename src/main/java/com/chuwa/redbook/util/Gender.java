package com.chuwa.redbook.util;

import java.util.HashMap;
import java.util.Map;

public enum Gender {
    FEMALE(0), MALE(1), UNSPECIFIC(2);

    private int code;
    private static Map<Integer, Gender> genderMap;

    Gender(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Gender getGender(int code) {
        if (genderMap == null) {
            genderMap = new HashMap<>();
            genderMap.put(0, Gender.FEMALE);
            genderMap.put(1, Gender.MALE);
            genderMap.put(2, Gender.UNSPECIFIC);
        }
        return genderMap.get(code);
    }
}
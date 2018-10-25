package com.berlioz;

import java.util.ArrayList;

public class ListHelper {

    public static ArrayList<String> Path() {
        return new ArrayList<String>();
    }

    public static ArrayList<String> Path(String v1) {
        ArrayList<String> list = Path();
        list.add(v1);
        return list;
    }

    public static ArrayList<String> Path(String v1, String v2) {
        ArrayList<String> list = Path(v1);
        list.add(v2);
        return list;
    }

    public static ArrayList<String> Path(String v1, String v2, String v3) {
        ArrayList<String> list = Path(v1, v2);
        list.add(v3);
        return list;
    }
}

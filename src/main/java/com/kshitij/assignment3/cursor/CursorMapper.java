package com.kshitij.assignment3.cursor;

import java.util.HashMap;

public class CursorMapper {
    private static CursorMapper singleInstance = null;

    private HashMap<String,Cursor> map;

    private CursorMapper() {
        this.map = new HashMap<>();
    }

    public static CursorMapper CursorMapper() {
        if(singleInstance == null) {
            singleInstance = new CursorMapper();
        }
        return singleInstance;
    }

    public boolean put(String key, Cursor cursor) {
        if(map.containsKey(key)) {
            map.put(key,cursor);
        }
        map.put(key,cursor);
        return true;
    }

    public boolean remove(String key) {
        if(!map.containsKey(key)) {
            map.remove(key);
        }
        return true;
    }

    public void notifyCursor(String key) {
        if (map.containsKey(key)) {
            Cursor cursor = map.get(key);
            cursor.updateObserver();
        }
    }
}

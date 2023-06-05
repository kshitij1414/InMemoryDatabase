package com.kshitij.assignment3.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kshitij.assignment3.database.array.Array;
import com.kshitij.assignment3.database.dbobject.CustomObject;
import com.kshitij.assignment3.decorator.DatabaseClient;
import com.kshitij.assignment3.fileio.FileOperations;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {
    private HashMap<String, Object> db;

    private DatabaseClient databaseClient;
    public Storage(DatabaseClient databaseClient) {
        db = new HashMap<>();
        this.databaseClient = databaseClient;
    }

    public boolean put(String key, Object value) {
        db.put(key,value);
        return true;
    }


    public Object get(String key) {
        return db.get(key);
    }

    public Object remove(String key) {
        return db.remove(key);
    }

    public boolean containsKey(String key) {
        return db.containsKey(key);
    }

    public int size() {
        return db.size();
    }

    public void snapshot(){
        FileOperations fileOperation = new FileOperations();
        System.out.println("Memento" + toString());
        try {
            fileOperation.clearFile(new File("dbSnapshot.txt"));
            fileOperation.writeData(new File("dbSnapshot.txt"),toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileOperation.clearFile(new File("commands.txt"));

    }

    public void snapshot(File commands, File dbSnapshot){
        FileOperations fileOperation = new FileOperations();
        try {
            fileOperation.clearFile(dbSnapshot);
            fileOperation.writeData(dbSnapshot,toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        clears the commands from file after backup
        FileOperations.clearFile(commands);
    }

    public String toString() {
        HashMap<String,Object> map = convertNestedObjectToPrimitiveType();
        Gson gson = new Gson();
        String toJson = gson.toJson(map);
        return toJson;
    }

    public HashMap<String, Object> fromString(String value) {
        Type userListType = new TypeToken<HashMap<String,Object>>(){}.getType();
        Gson gson = new Gson();
        HashMap object = gson.fromJson(value, userListType);
        HashMap<String,Object> newCustomObject = new HashMap<>();
        object.forEach((k, v)
                -> {
            if (v instanceof ArrayList) {
                Array ab = new Array();
                Array a = ab.fromString(v.toString());
                newCustomObject.put(k.toString(),a);
                System.out.println(a);
            }else if (v instanceof Map) {
                CustomObject a = (new CustomObject()).fromString(v.toString());
                newCustomObject.put(k.toString(),a);
                System.out.println(a);
            } else {
                newCustomObject.put(k.toString(),v);
            }
        });
        return newCustomObject;
    }

    public HashMap<String,Object> convertNestedObjectToPrimitiveType() {
        HashMap<String,Object> map = new HashMap<>();
        db.forEach((k,v)-> {
            if (v instanceof Array) {
                Array arr = new Array();
                ArrayList a = arr.convertToArrayList((Array)v);
                map.put(k,a);
            }else if (v instanceof CustomObject) {
                HashMap a = (new CustomObject()).convertToHashMap((CustomObject) v);
                map.put(k,a);
            } else {
                map.put(k,v);
            }
        });
        return map;
    }

    public void recover(){
        FileOperations fileOperation = new FileOperations();
        List<String> restoredDB =
                null;
        try {
            restoredDB =  fileOperation.readData(new File("dbSnapshot.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(restoredDB != null && restoredDB.size() != 0 && restoredDB.get(1).length() > 0) {
            db = fromString(restoredDB.get(1));
        }

        databaseClient.executeSavedOperations(null);
    }

    public void recover(File commands, File dbSnapshot){
        FileOperations fileOperation = new FileOperations();
        List<String> restoredDB =
                null;
        try {
            restoredDB =  fileOperation.readData(dbSnapshot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(restoredDB != null) {
            db = fromString(restoredDB.get(0));
        }

        databaseClient.executeSavedOperations(commands);

    }
}

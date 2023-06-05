package com.kshitij.assignment3.database.dbobject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kshitij.assignment3.cursor.CursorMapper;
import com.kshitij.assignment3.database.array.Array;
import com.kshitij.assignment3.database.array.IArray;
import com.kshitij.assignment3.exception.IncompatibleType;
import com.kshitij.assignment3.exception.KeyNotFoundException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class CustomObject implements ICustomObject,Cloneable {
    public HashMap<String,Object> map = new HashMap<>();
    Gson gson = new Gson();
    private String parent = "";
    private CursorMapper mapper = CursorMapper.CursorMapper();


    public CustomObject() {
    }

    public Object clone()
    {
        return fromString(toString());
    }

    public CustomObject(HashMap<String,Object> value) {
        this.map = value;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setParentForNestedValue(String parent) {
        setParent(parent);
        map.forEach((k,v)->{
            if(v instanceof Array) {
                ((Array)v).setParentForNestedValue(parent );
            } else if(v instanceof CustomObject) {
                ((CustomObject) v).setParentForNestedValue(parent );
            }
        });
    }

    public String getParent() {
        return this.parent;
    }

    public boolean put(String key, Object object) {
        if (map.containsKey(key)) {
            throw new KeyNotFoundException("Key exixts");
        }

        if (object instanceof Array) {
            ((Array)object).setParentForNestedValue(getParent());
        } else if (object instanceof CustomObject) {
            ((CustomObject)object).setParentForNestedValue(getParent());
        }
        map.put(key,object);

        mapper.notifyCursor(getParent());
        return true;
    }

    public Object get(String key) {
        if (!map.containsKey(key)) {
            throw new KeyNotFoundException("Key not found");
        }

        return map.get(key);
    }

    public HashMap<String,Object> convertToHashMap(CustomObject object) {
        HashMap<String,Object> map = new HashMap<>();

        object.map.forEach((k,v) -> {
            if (v instanceof Array) {
                Array arr = new Array();
                ArrayList a = arr.convertToArrayList((Array)v);
                map.put(k,a);
            }else if (v instanceof CustomObject) {
                HashMap a = this.convertToHashMap((CustomObject) v);
                map.put(k,a);
            } else {
                map.put(k,v);
            }
        });
        return map;
    }

    public String toString(){
        HashMap map = convertToHashMap(this);
        return gson.toJson(map);
    }

    public Integer getInt(String key) {
        if (!map.containsKey(key)) {
            throw new KeyNotFoundException("Key not found");
        }

        if (!(map.get(key) instanceof Integer) ) {
            throw new IncompatibleType("The value is associated with this index is not an Integer");
        }

        return (Integer)map.get(key);
    }

    public IArray getArray(String key) {
        if (map.containsKey(key)) {
            throw new KeyNotFoundException("Index Out Of Bounds");
        }

        if (!(map.get(key) instanceof Array) ) {
            throw new IncompatibleType("The value is associated with this index is not an Array");
        }
        return (Array) map.get(key);

    }

    public ICustomObject getObject(String key) {
        if (map.containsKey(key)) {
            throw new KeyNotFoundException("Index Out Of Bounds");
        }

        if (!(map.get(key) instanceof CustomObject) ) {
            throw new IncompatibleType("The value is associated with this index is not an Object");
        }
        return (CustomObject) map.get(key);

    }

    public int length() {
        return this.map.size();
    }

    public CustomObject fromString(String value) {
        Type userListType = new TypeToken<HashMap<String,Object>>(){}.getType();
        HashMap object = gson.fromJson(value, userListType);
        CustomObject newCustomObject = new CustomObject();

        object.forEach((k, v)
                -> {
                if (v instanceof ArrayList) {
                    Array ab = new Array();
                    Array a = ab.fromString(v.toString());
                    newCustomObject.put(k.toString(),a);
                    System.out.println(a);
                }else if (v instanceof Map) {
                CustomObject a = this.fromString(v.toString());
                newCustomObject.put(k.toString(),a);
            } else {
                    newCustomObject.put(k.toString(),v);
                }
        });
        return newCustomObject;
    }

    public Object remove(String key) {
        Object obj = map.remove(key);
        mapper.notifyCursor(getParent());
        return obj;
    }

}

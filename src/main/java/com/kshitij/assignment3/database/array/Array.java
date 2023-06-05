package com.kshitij.assignment3.database.array;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kshitij.assignment3.cursor.CursorMapper;
import com.kshitij.assignment3.database.dbobject.CustomObject;
import com.kshitij.assignment3.database.dbobject.ICustomObject;
import com.kshitij.assignment3.exception.IncompatibleType;
import com.kshitij.assignment3.exception.KeyNotFoundException;
import java.lang.reflect.Type;
import java.util.*;

public class Array implements IArray,Cloneable {
    private List<Object> value = new ArrayList<>();
    private Gson gson = new Gson();
    private CursorMapper mapper = CursorMapper.CursorMapper();
    private String parent = "";

    public Array() {
    }

    public Object clone()
    {
        return fromString(toString());
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setParentForNestedValue(String parent) {
        int index = 0;
        setParent(parent);
        value.forEach((v)->{
            if(v instanceof Array) {
                ((Array)v).setParentForNestedValue(parent);
            } else if (v instanceof CustomObject) {
                ((CustomObject)v).setParentForNestedValue(parent
                );
            }
        });
    }

    public String getParent() {
        return this.parent;
    }

    public boolean put(Object object) {
        if (object instanceof Array) {
            ((Array)object).setParentForNestedValue(getParent());
        } else if (object instanceof CustomObject) {
            ((CustomObject)object).setParentForNestedValue(getParent());
        }

        value.add(object);

        mapper.notifyCursor(getParent());
        return true;
    }


    public Object get(int index) {
        if (index>= value.size()) {
            throw new KeyNotFoundException("Index Out Of Bounds");
        }
        return value.get(index);
    }

    public String getString(int index) {
        if (index>= value.size()) {
            throw new KeyNotFoundException("Index Out Of Bounds");
        }

        if (!(value.get(index) instanceof String) ) {
            throw new IncompatibleType("The value is associated with this index is not an Integer");
        }
        return (String)value.get(index);
    }

    public Integer getInt(int index) {
        if (index>= value.size()) {
            throw new KeyNotFoundException("Index Out Of Bounds");
        }

        if (!(value.get(index) instanceof Integer) ) {
            throw new IncompatibleType("The value is associated with this index is not an Integer");
        }
        return (Integer)value.get(index);
    }

    public IArray getArray(int index) {
        if (index>= value.size()) {
            throw new KeyNotFoundException("Index Out Of Bounds");
        }

            if (!(value.get(index) instanceof Array) ) {
            throw new IncompatibleType("The value is associated with this index is not an Array");
        }
        return (Array) value.get(index);

    }

    public ICustomObject getObject(int index) {
        if (index>= value.size()) {
            throw new KeyNotFoundException("Index Out Of Bounds");
        }

        if (!(value.get(index) instanceof CustomObject) ) {
            throw new IncompatibleType("The value is associated with this index is not an Integer");
        }

        return (CustomObject) value.get(index);
    }

    public int length() {
        return value.size();
    }

    public Object remove(int index) {
        if(index<= value.size()-1) {
            return value.remove(index);
        }
        mapper.notifyCursor(getParent());
        return null;
    }

    public ArrayList<Object> convertToArrayList(Array array) {
        ArrayList arrayList = new ArrayList<>();

        array.value.forEach(v -> {
            if (v instanceof Array){
                arrayList.add(convertToArrayList((Array)v));
            }else if (v instanceof CustomObject) {
                arrayList.add(new CustomObject().convertToHashMap((CustomObject) v));
            } else {
                arrayList.add(v);
            }
        });
        return arrayList;
    }

    public String toString() {
        return gson.toJson(convertToArrayList(this));
    }

    public Array fromString(String value) {
        Array array = new Array();
        Type expectedType = new TypeToken<ArrayList<Object>>(){}.getType();
        ArrayList object = gson.fromJson(value, expectedType);

        object.forEach((v)
                -> {
            if (v instanceof ArrayList) {
                Array a = this.fromString(v.toString());
                array.put(a);
            }else if (v instanceof Map) {
                CustomObject db = new CustomObject();
                CustomObject a = db.fromString(v.toString());
                array.put(a);
            } else {
                array.put(v);
            }
        });
        return array;
    }

}

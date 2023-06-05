package com.kshitij.assignment3.database;

import com.kshitij.assignment3.database.array.IArray;
import com.kshitij.assignment3.database.dbobject.CustomObject;
import com.kshitij.assignment3.database.dbobject.ICustomObject;

public interface IDatabase {

    public boolean put(String key, Object value);

    public Object get(String key);

    public int getInt(String key);

    public String getString(String key);

    public IArray getArray(String key);

    public ICustomObject getObject(String key);

    public Object remove(String key);


}

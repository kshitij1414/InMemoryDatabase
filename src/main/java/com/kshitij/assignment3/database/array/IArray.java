package com.kshitij.assignment3.database.array;

import com.kshitij.assignment3.database.dbobject.ICustomObject;

public interface IArray {
    public boolean put(Object object);

    public Object get(int index);

    public String getString(int index);

    public Integer getInt(int index);

    public IArray getArray(int index);

    public ICustomObject getObject(int index);

    public Object remove(int index);
}

package com.kshitij.assignment3.database.dbobject;

public interface ICustomObject {
    public Object get(String key);

    public boolean put(String key, Object value);

    public Object remove(String key);

    public ICustomObject getObject(String key);
}

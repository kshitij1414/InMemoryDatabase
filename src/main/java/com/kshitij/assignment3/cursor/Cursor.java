package com.kshitij.assignment3.cursor;

import com.kshitij.assignment3.database.Database;
import com.kshitij.assignment3.database.array.Array;
import com.kshitij.assignment3.database.dbobject.CustomObject;
import com.kshitij.assignment3.exception.IncompatibleType;
import com.kshitij.assignment3.exception.KeyNotFoundException;
import com.kshitij.assignment3.observer.IObserver;
import com.kshitij.assignment3.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Cursor {
    private String key;
    private Database db;
    private Object currentValue;

    public List<IObserver> observers = new ArrayList<>();

    private CursorMapper mapper = CursorMapper.CursorMapper();
    public Cursor(String key, Database db) throws CloneNotSupportedException{

        this.db = db;
        this.key = key;
        currentValue = this.db.get(key);

        if (currentValue instanceof Array) {
            currentValue = ((Array) db.get(key)).clone();
        } else if(currentValue instanceof CustomObject) {
            currentValue = ((CustomObject)this.db.get(key)).clone();
        }

        mapper.put(key,this);
    }
    
    public boolean updateObserver() {
        String message = "";
        try {
            Object newValue = db.get(key);
            message = "The " + key + " in the database has been updated. Old Value :- " + currentValue.toString() + " New Value :- " + newValue.toString();

        } catch(KeyNotFoundException e) {
            message = "The " + key + " has been removed from the database";
        }


        String finalMessage = message;
        observers.forEach((o)->o.update(finalMessage));
        return true;
    }

    public boolean addObserver(IObserver observer) {
        observers.add(observer);

        return true;
    }

    public boolean removeObserver(Observer observer) {
        observers.remove(observer);
        return true;
    }

    public Object get() {
        return currentValue;
    }

    public Integer getInt() {
        if(!(currentValue instanceof Integer)) {
            throw new IncompatibleType("The current value is not an integer");
        }
        return (Integer) currentValue;
    }

    public String getString() {
        if(!(currentValue instanceof String)) {
            throw new IncompatibleType("The current value is not a string");
        }
        return (String) currentValue;
    }

    public Array getArray() {
        if(!(currentValue instanceof Array)) {
            throw new IncompatibleType("The current value is not an Array");
        }
        return (Array) currentValue;
    }

    public CustomObject getObject() {
        if(!(currentValue instanceof CustomObject)) {
            throw new IncompatibleType("The current value is not a custom object");
        }
        return (CustomObject) currentValue;
    }
}

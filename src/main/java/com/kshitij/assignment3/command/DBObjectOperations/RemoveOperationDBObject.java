package com.kshitij.assignment3.command.DBObjectOperations;

import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.dbobject.CustomObject;

import java.io.Serializable;

public class RemoveOperationDBObject implements IDatabaseOperation, Serializable {
    private CustomObject customObject;
    private String key;
    private Object removedValue;

    public RemoveOperationDBObject(String key) {
        this.key = key;
    }

    public Object execute(Object object) {
        customObject = (CustomObject) object;
        return customObject.remove(key);
    }

    public Object undo() {
        return customObject.put(key, removedValue);
    }

    public String toString() {
        return operationToString("INSERT", customObject.getParent());
    }

    public Object getValue() {
        return customObject;
    }
}

package com.kshitij.assignment3.command.DBObjectOperations;

import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.dbobject.CustomObject;

import java.io.Serializable;

public class PutOperationDBObject implements IDatabaseOperation, Serializable {
    private Object value;
    private CustomObject customObject;
    private String key;

    public PutOperationDBObject(String key,Object value) {
        this.key = key;
        this.value = value;
    }

    public Object execute(Object object) {
        customObject = (CustomObject) object;
        return customObject.put(key, value);
    }
    public Object undo() {
        return customObject.remove(key);
    }

    public String toString() {
        return operationToString("INSERT", customObject.getParent());
    }

    public Object getValue() {
        return customObject;
    }
}

package com.kshitij.assignment3.command.DBOperations;

import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.Database;

import java.io.Serializable;

public class RemoveOperationDatabase implements IDatabaseOperation, Serializable {
    private Database db;
    private String key;
    private Object removedValue;

    public RemoveOperationDatabase(String key) {
        this.key = key;
    }

    @Override
    public Object execute(Object db) {
        this.db = (Database) db;
        removedValue = this.db.remove(key);
        return removedValue;
    }

    public Object undo() {
        return db.put(key, removedValue);
    }

    public String toString() {
        return operationToString("REMOVE", key);
    }

    public Object getValue() {
        return null;
    }



}

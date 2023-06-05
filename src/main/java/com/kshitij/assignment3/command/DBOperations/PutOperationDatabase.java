package com.kshitij.assignment3.command.DBOperations;

import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.Database;

import java.io.Serializable;

public class PutOperationDatabase implements IDatabaseOperation, Serializable {

    private final Object object;
    private Database db;
    private String key;

    public PutOperationDatabase(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    @Override
    public Object execute(Object db) {
        this.db = (Database) db;
        return this.db.put(key, object);
    }

    public Object undo() {
        return db.remove(key);
    }

    public String toString() {
        return operationToString("INSERT", key);
    }
    public Object getValue() {
        return db;
    }
    public String getKey() {
        return key;
    }


}

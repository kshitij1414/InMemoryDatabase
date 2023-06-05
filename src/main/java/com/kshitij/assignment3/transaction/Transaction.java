package com.kshitij.assignment3.transaction;

import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.array.Array;
import com.kshitij.assignment3.database.dbobject.CustomObject;
import com.kshitij.assignment3.database.IDatabase;
import com.kshitij.assignment3.database.Database;
import com.kshitij.assignment3.database.dbobject.ICustomObject;
import com.kshitij.assignment3.decorator.DatabaseClient;

import java.util.Stack;

public class Transaction implements IDatabase {

    private Database db;
    private DatabaseClient databaseClient;

    private Boolean isActive = true;
    Stack<IDatabaseOperation> operations = new Stack<>();
    public Transaction(Database db){
        this.db = db;
        databaseClient = new DatabaseClient(this.db,operations);
    }
    public boolean put(String key, Object value) {
        return databaseClient.put(key,value);
    }

    public int getInt(String key) {
        return this.db.getInt(key);
    }

    public String getString(String key) {
        return this.db.getString(key);
    }

    public Array getArray(String key) {
        return this.db.getArray(key);
    }

    public ICustomObject getObject(String key) {
        return this.db.getObject(key);
    }


    public Object get(String key) {
        return this.db.get(key);
    }

    public Object remove(String key) {
        return this.databaseClient.remove(key);
    }

    public boolean abort() {
        this.operations = this.databaseClient.getCommands();

        while(!this.operations.isEmpty()) {
            IDatabaseOperation operation = this.operations.pop();
            operation.undo();
        }
        this.isActive = false;

        databaseClient.snapshot();
        return true;
    }

    public boolean commit() {
        databaseClient.commitCommands();
        this.isActive = false;
        return true;
    }


}

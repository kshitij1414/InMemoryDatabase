package com.kshitij.assignment3.decorator;

import com.kshitij.assignment3.command.DBOperations.PutOperationDatabase;
import com.kshitij.assignment3.command.DBOperations.RemoveOperationDatabase;
import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.cursor.Cursor;
import com.kshitij.assignment3.database.*;
import com.kshitij.assignment3.database.array.Array;
import com.kshitij.assignment3.database.array.IArray;
import com.kshitij.assignment3.database.dbobject.CustomObject;
import com.kshitij.assignment3.database.dbobject.ICustomObject;
import com.kshitij.assignment3.transaction.Transaction;

import java.io.File;
import java.util.*;

public class DatabaseClient extends Client implements IDatabase {

    private Database db;
    private Stack<IDatabaseOperation> stack ;

    private File commandFile;
    public DatabaseClient(Database db) {
        super(true);
        this.db = db;
        this.isSavedOperation = false;
    }

    public DatabaseClient(Database db, File commandFile) {
        super(true);
        this.db = db;
        this.isSavedOperation = false;
        this.commandFile = commandFile;
    }

    public DatabaseClient(Database db, Stack<IDatabaseOperation> operations) {
        super(true);
        this.db = db;
        this.stack = operations;
        this.isSavedOperation = false;
    }



    public void executeSavedOperations(File commandFile) {
        List<List<String>> operations;

        if (commandFile == null) {
            operations = retrieveOperations(new File("commands.txt"));
        } else {
            operations = retrieveOperations(commandFile);
        }
        for (List<String> operation : operations) {
            executeOperation(operation);
        }
    }

    public void executeOperation(List<String> operations) {
        String operation = operations.get(0);
        String key = operations.get(1);
        String value = operations.get(2);

        switch(operation) {
            case "INSERT" :
                if(db.contains(key)) {
                    db.remove(key);
                }
                put(key,parseValue(value));
                break;
            case "REMOVE" :
                remove(key);
        }
    }

    public Object parseValue(String value) {
        if (value.charAt(0) == '[') {
            return new Array().fromString(value);
        } else if (value.charAt(0) == '{') {
            return new CustomObject().fromString(value);
        } else if(value.length() > 8 && value.substring(0,8) == "INTEGER("){
            String intValue = value.substring(7,value.length()-1);
            return Integer.parseInt(intValue);
        } else {
            return value;
        }
    }

    public String toString() {
        return this.db.toString();
    }
    public boolean put(String key, Object value) {
        IDatabaseOperation put = new PutOperationDatabase(key,value);
        Object newValue = value;
        if(value instanceof Integer) {
            newValue = "INTEGER(" + value + ")";
        }
        put.execute(this.db);
        if (stack == null) {
            writeToFile(put+ "----" +  newValue.toString());
        } else {
            this.stack.add(put);

        }

        return true;
    }

    public Object get(String key) {
        return this.db.get(key);
    }

    public int getInt(String key) {
        return this.db.getInt(key);
    }

    public String getString(String key){
        return this.db.getString(key);
    }

    public IArray getArray(String key) {
        return new ArrayClient(db.getArray(key),db);
    }

    public ICustomObject getObject(String key){
        return new CustomObjectClient(db.getObject(key),db);
    }

    public Object remove(String key) {
        IDatabaseOperation remove = new RemoveOperationDatabase(key);

        Object value = remove.execute(this.db);

        if (stack == null) {
            writeToFile(remove+ "----" + "NULL");
        } else {
            this.stack.add(remove);

        }
        return value;
    }

    public Cursor getCursor(String key) {
        return this.db.getCursor(key);
    }

    public Stack<IDatabaseOperation> getCommands() {
        return stack;
    }
    public boolean clearCommands() {
        this.stack = new Stack<>();
        return true;
    }

    public Transaction transaction() {
        return this.db.transaction();
    }

    public void commitCommands() {
        this.stack.forEach((operation) -> {
            String value = "";
            if(operation instanceof PutOperationDatabase) {
                String key = ((PutOperationDatabase)operation).getKey();
                value = ((Database)(operation.getValue())).get(key).toString();
            } else if(operation instanceof  RemoveOperationDatabase) {
                value = "NULL";
            } else {
                Object object = operation.getValue();
                String parent = "";
                if (object instanceof Array) {
                    parent = ((Array) object).getParent();
                } else if (object instanceof CustomObject) {
                    parent = ((CustomObject) object).getParent();
                }
                value = db.get(parent).toString();
            }
            writeToFile(operation.toString() + "----" + value);
        });
    }

    public void snapshot() {
        db.snapshot();
    }

}

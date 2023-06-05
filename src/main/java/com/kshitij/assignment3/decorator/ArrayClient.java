package com.kshitij.assignment3.decorator;

import com.kshitij.assignment3.command.ArrayOperations.PutOperationArray;
import com.kshitij.assignment3.command.ArrayOperations.RemoveOperationArray;
import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.Database;
import com.kshitij.assignment3.database.array.Array;
import com.kshitij.assignment3.database.dbobject.CustomObject;
import com.kshitij.assignment3.database.array.IArray;
import com.kshitij.assignment3.database.dbobject.ICustomObject;

public class ArrayClient extends Client implements IArray {
    private Array array;

    private Database database;
    public ArrayClient(IArray array) {
        super(false);
        this.array = (Array)array;
    }
    public ArrayClient(IArray array, Database database) {
        super(false);
        this.array = (Array)array;
        this.database = database;
    }

    public boolean put(Object value) {
        IDatabaseOperation put = new PutOperationArray(value);

        Boolean response = (boolean)put.execute(this.array);

        if(database != null) {
            writeToFile(put + "----" + database.get(array.getParent()).toString());
        }

        return response;
    }

    public Object get(int index) {
        return this.array.get(index);
    }

    public Integer getInt(int index) {
        return this.array.getInt(index);
    }

    public String getString(int index){
        return this.array.getString(index);
    }

    public IArray getArray(int index) {
        return this.array.getArray(index);
    }

    public ICustomObject getObject(int index){
        return new CustomObjectClient((CustomObject) this.array.getObject(index));
    }

    public String toString() {
        return this.array.toString();
    }

    public Object remove(int index) {
        IDatabaseOperation remove = new RemoveOperationArray(index);

        Object value = remove.execute(this.array);

        if(database != null) {
            writeToFile(remove + "----" + database.get(array.getParent()).toString());
        }
        return value;
    }




}

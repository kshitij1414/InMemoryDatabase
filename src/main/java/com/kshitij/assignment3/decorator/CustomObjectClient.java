package com.kshitij.assignment3.decorator;

import com.kshitij.assignment3.command.DBObjectOperations.PutOperationDBObject;
import com.kshitij.assignment3.command.DBObjectOperations.RemoveOperationDBObject;
import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.Database;
import com.kshitij.assignment3.database.array.IArray;
import com.kshitij.assignment3.database.dbobject.CustomObject;
import com.kshitij.assignment3.database.dbobject.ICustomObject;
import com.kshitij.assignment3.fileio.FileOperations;

public class CustomObjectClient extends Client implements ICustomObject {
    private CustomObject customObject;
    private FileOperations fileOperation;

    private Database database;
    public CustomObjectClient(ICustomObject customObject) {
        super(false);
        this.customObject = (CustomObject) customObject;
        this.fileOperation = new FileOperations();
    }

    public CustomObjectClient(ICustomObject customObject, Database database) {
        super(false);
        this.customObject = (CustomObject) customObject;
        this.fileOperation = new FileOperations();
        this.database = database;
    }

    public boolean put(String key, Object value) {
        IDatabaseOperation put = new PutOperationDBObject(key,value);

        Boolean res = (boolean)put.execute(this.customObject);

        if(database != null) {
            writeToFile(put + "----" + database.get(customObject.getParent()).toString());
        }
        return res;
    }

    public Object get(String key) {
        return this.customObject.get(key);
    }

    public int getInt(String key) {
        return this.customObject.getInt(key);
    }

    public String toString(){
        return this.customObject.toString();
    }

    public IArray getArray(String key) {
        return new ArrayClient(this.customObject.getArray(key));
    }

    public ICustomObject getObject(String key){
        return new CustomObjectClient(this.customObject.getObject(key));
    }

    public Object remove(String key) {
        IDatabaseOperation remove = new RemoveOperationDBObject(key);

        Object value = remove.execute(this.customObject);

        if(database != null) {
            writeToFile(remove + "----" + database.get(customObject.getParent()).toString());
        }
        return value;
    }

}

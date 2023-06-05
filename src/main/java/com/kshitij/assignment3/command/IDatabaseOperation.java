package com.kshitij.assignment3.command;

import com.kshitij.assignment3.database.Database;
import com.kshitij.assignment3.database.array.Array;
import com.kshitij.assignment3.database.dbobject.CustomObject;

public interface IDatabaseOperation {

    Object execute (Object object);

    Object undo();

    default String operationToString(String operation, String key) {
        return operation + "----" + key;
    }

    Object getValue();
}

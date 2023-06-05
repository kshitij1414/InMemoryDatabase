package com.kshitij.assignment3.command.ArrayOperations;

import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.array.Array;

import java.io.Serializable;

public class PutOperationArray implements IDatabaseOperation, Serializable {
    private Object value;
    private Array array;
    private int index;

    public PutOperationArray(Object value) {
        this.value = value;
    }

    public Object execute(Object array) {
        this.array = (Array) array;
        this.index = this.array.length();
        return this.array.put(value);
    }

    public Object undo() {
        return array.remove(index);
    }

    public String toString() {
        return operationToString("INSERT", array.getParent());
    }

    public Object getValue() {
        return array;
    }

}

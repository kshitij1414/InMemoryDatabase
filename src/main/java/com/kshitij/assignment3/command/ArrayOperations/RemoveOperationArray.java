package com.kshitij.assignment3.command.ArrayOperations;

import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.array.Array;

import java.io.Serializable;

public class RemoveOperationArray implements IDatabaseOperation, Serializable {
    private int index;
    private Array array;
    private Object removedValue;

    public RemoveOperationArray(int index) {
        this.index = index;
    }

    public Object execute(Object array) {
        this.array = (Array) array;
        removedValue =  this.array.remove(index);
        return removedValue;
    }

    public Object undo() {
        return array.put(removedValue);
    }

    public String toString() {
        return operationToString("INSERT", array.getParent());
    }

    public Object getValue() {
        return array;
    }
}

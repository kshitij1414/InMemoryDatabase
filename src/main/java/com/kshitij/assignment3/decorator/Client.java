package com.kshitij.assignment3.decorator;

import com.kshitij.assignment3.command.IDatabaseOperation;
import com.kshitij.assignment3.database.Database;
import com.kshitij.assignment3.fileio.FileOperations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Client {
    private Stack<IDatabaseOperation> operationList;
    private Database db;
    private DatabaseClient databaseClient;
    private FileOperations fileOperation;
    protected Boolean isSavedOperation ;

    protected File commandFile;
    public Client(Boolean isSavedOperation) {
        fileOperation = new FileOperations();
        this.isSavedOperation = isSavedOperation;
    }

    public Client(Boolean isSavedOperation, File commandFile) {
        fileOperation = new FileOperations();
        this.isSavedOperation = isSavedOperation;
        this.commandFile = commandFile;
    }

    public List<List<String>> retrieveOperations(File file ) {
        List<List<String>> commands = new ArrayList<>();
        try {
            List<String> operations = fileOperation.readData(file);


            for (String operation : operations) {
                if(operation.length()>0) {
                    String[] res = operation.split("----");
                    List<String> resp = Arrays.stream(res).collect(Collectors.toList());
                    commands.add(resp);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileOperation.clearFile(file);
        return commands;
    }
    public void writeToFile( String operation) {
        if (commandFile == null) {
            commandFile = new File("commands.txt");
        }

        try {
            fileOperation.writeData(commandFile,operation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.kshitij.assignment3.fileio;

import java.io.*;
import java.util.*;

public class FileOperations {

    static String PATH_TO_FILE = "commands.txt";

    public List<String> readData(File file) throws IOException {
        List<String> result = new ArrayList<>();
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(file);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                result.add(line);
            }

            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
//        clearFile(new File(PATH_TO_FILE));
        return result;
    }

    public static void clearFile(File file) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter printWriter = new PrintWriter(fileWriter, false);
        printWriter.flush();
        printWriter.close();

        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean writeData(File file,String value) throws IOException {

        FileOutputStream outputStream = new FileOutputStream(file, true);
        byte[] strToBytes = value.getBytes();
        outputStream.write(System.getProperty("line.separator").getBytes());
        outputStream.write(strToBytes);

        outputStream.close();
        return true;
    }
}

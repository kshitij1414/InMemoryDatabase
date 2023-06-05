package com.kshitij.assignment3.database;
import com.kshitij.assignment3.database.array.Array;
import com.kshitij.assignment3.database.dbobject.CustomObject;
import com.kshitij.assignment3.decorator.ArrayClient;
import com.kshitij.assignment3.decorator.DatabaseClient;
import com.kshitij.assignment3.fileio.FileOperations;
import com.kshitij.assignment3.transaction.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    void testSFromString() {

        Array firstArr = new Array();
        ArrayClient array = new ArrayClient(firstArr);

        CustomObject secondObj = new CustomObject();
        CustomObject thirdObj = new CustomObject();

        secondObj.put("Kp",1);
        firstArr.put(secondObj);
        DatabaseClient db = new DatabaseClient(new Database());
        System.out.println(db.toString());
//        db.put("KP",firstArr);
//        db.getArray("KP").getObject(0).put("Abhi",thirdObj);
//        Cursor cursor = db.getCursor("KP");
//        Observer o = new Observer();
//        cursor.addObserver(o);
//        db.getArray("KP").put("Anuj");

        Transaction tr = db.transaction();
//        db.put("Kshitij","1");
//                db.put("Anuj",firstArr);
//                db.getArray("Anuj").remove(0);

//        System.out.println(db.get("Anuj"));
        tr.put("KshitijPoojary", 1);
        tr.commit();
//        System.out.println(db);
////        System.out.println();
////        System.out.println(db.getArray("KP").get(1));
//        System.out.println(db.get("KP"));
        FileOperations fileOperation = new FileOperations();
////        Object commandHistory = fileOperation.readNext();
//        try {
//            List<String> res = fileOperation.readData();
////            Gson gson = new Gson();
////            Type expectedType = new TypeToken<ArrayList<Object>>(){}.getType();
////            ArrayList object = gson.fromJson(res.get(0), expectedType);
//            Array abc = new Array();
////            String[] r = res.get(1).split(":");
////            Array ac = abc.fromString(r[r.length-1]);
////            System.out.println(ac.get(0));
////            System.out.println(ac.get(1));
////            System.out.println(bc.getParent());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        assertEquals(true,true);
    }

    @Test
    void testNew() {

        Array array = new Array();
        array.put("Anuj");
        array.put(26);

        Array dob = new Array();
        dob.put("March");
        dob.put(26);
        dob.put(1996);

        CustomObject customObject = new CustomObject();
        customObject.put("CustomDOB", dob);

        DatabaseClient databaseExecutor = new DatabaseClient(new Database());
        databaseExecutor.put("Name", array);
        databaseExecutor.put("DOB", customObject);


        databaseExecutor.getArray("Name").put("Kshitij");
        databaseExecutor.put("AGE", 30);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        CustomObject newCO = new CustomObject();
        newCO.put("Test1", "test1");
        newCO.put("Test2", "test2");

        databaseExecutor.put("TEST", newCO);

        databaseExecutor.getObject("TEST").remove("Test2");

        System.out.println(databaseExecutor);
    }
}

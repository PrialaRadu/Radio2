package com.example.radio.practic.domain;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class IDGenerator {
    public static int productId;
    public static int orderId;
    public static final String FILE_PATH = "./data/ids.txt";

    static{
        initializeIds();
    }

    private static void initializeIds(){
        Properties prop = new Properties();
        try(FileInputStream fis = new FileInputStream(FILE_PATH)){
            prop.load(fis);
            productId = Integer.parseInt(prop.getProperty("comandaId", "100"));
            orderId = Integer.parseInt(prop.getProperty("tortId", "100"));
        }
        catch(IOException e){
            System.out.println("The file can't be read!! Initializing IDs with 100");
        }
    }

    private static void saveIDs(){
        Properties prop = new Properties();
        prop.setProperty("comandaId", Integer.toString(productId));
        prop.setProperty("tortId", Integer.toString(orderId));

        try(FileOutputStream out = new FileOutputStream(FILE_PATH)){
            prop.store(out, "Last IDs");
        } catch(IOException e){
            System.out.println("The file can't be written!!");
        }
    }

    public static int generatorProductID(){
        ++productId;
        saveIDs();
        return productId;
    }

    public static int generatorOrderID(){
        ++orderId;
        saveIDs();
        return orderId;
    }
}

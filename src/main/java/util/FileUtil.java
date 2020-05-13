package util;

import model.Ad;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {

    private static final String AD_FILE_PATH = "src\\main\\resources\\ads.info";
    private static final String USER_FILE_PATH = "src\\main\\resources\\users.info";

    public static void serializeAdList(List<Ad> adList) {
        File adListFile = new File(AD_FILE_PATH);
        try {
            if (!adListFile.exists()) {
                adListFile.createNewFile();
            }
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(AD_FILE_PATH))) {
                outputStream.writeObject(adList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static List<Ad> deserializeAdList(){
        List<Ad> result = new ArrayList<>();
        File adListFile = new File(AD_FILE_PATH);
        if (adListFile.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(AD_FILE_PATH))) {
                Object tmpObj = inputStream.readObject();
                return (List<Ad>) tmpObj;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static void serializeUserMap(Map<String, User> userMap) {
        File userMapFile = new File(USER_FILE_PATH);
        try {
            if (!userMapFile.exists()) {
                userMapFile.createNewFile();
            }
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(USER_FILE_PATH))) {
                outputStream.writeObject(userMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, User> deserializeUserMap() {
        Map<String, User> result = new HashMap<>();
        File userMapFile = new File(USER_FILE_PATH);
        if (userMapFile.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(USER_FILE_PATH))) {
                Object tmpObj = inputStream.readObject();
                return (Map<String, User>) tmpObj;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}

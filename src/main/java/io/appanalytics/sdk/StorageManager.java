package io.appanalytics.sdk;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cem Sancak on 13.10.2015.
 */
class StorageManager {

    public final static StorageManager INSTANCE = new StorageManager();

    private String apiKey;
    private Context context;

    private StorageManager() { }

    public void initializeStorageManager(String apiKey, Context context) {
        this.apiKey = apiKey;
        this.context = context;
    }

    public void saveData(List<?> dataList) {
        ByteArrayOutputStream arrayOutputStream = null;
        ObjectOutputStream objectOutput = null;
        FileOutputStream fos = null;
        try {
            Identifier identifier = (Identifier) dataList.get(0);
            String description = identifier.getModelUrl();
            arrayOutputStream = new ByteArrayOutputStream();
            objectOutput = new ObjectOutputStream(arrayOutputStream);
            objectOutput.writeObject(dataList);
            byte[] data = arrayOutputStream.toByteArray();
            fos = context.openFileOutput(apiKey + description, Context.MODE_PRIVATE);
            fos.write(data);
        } catch (Exception e) {
            Log.e("AppAnalytics", "Save error");
        } finally {
            try {
                if (arrayOutputStream != null) {
                    arrayOutputStream.close();
                }
                if (objectOutput != null) {
                    objectOutput.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) { }
        }
    }

    public <T> List<T> loadData(Class<T> clazz) {
        FileInputStream fis = null;
        ByteArrayOutputStream arrayOutputStream = null;
        ByteArrayInputStream byteArray = null;
        ObjectInputStream in = null;
        List<T> dataList = null;
        try {
            Identifier identifier = (Identifier) clazz.newInstance();
            String description = identifier.getModelUrl();
            fis = context.openFileInput(apiKey + description);
            arrayOutputStream = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(b)) != -1) {
                arrayOutputStream.write(b, 0, bytesRead);
            }
            byteArray = new ByteArrayInputStream(arrayOutputStream.toByteArray());
            in = new ObjectInputStream(byteArray);
            dataList = (List<T>) in.readObject();
        } catch (Exception e) {
            Log.e("AppAnalytics", "Load error");
            dataList = new ArrayList<>();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (arrayOutputStream != null) {
                    arrayOutputStream.close();
                }
                if (byteArray != null) {
                    byteArray.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) { }
        }
        return dataList;
    }
}

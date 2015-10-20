package io.appanalytics.sdk;

import android.content.Context;
import android.util.Base64;
import android.util.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        try {
            Identifier identifier = (Identifier) dataList.get(0);
            String description = identifier.getModelUrl();
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutput = new ObjectOutputStream(arrayOutputStream);
            objectOutput.writeObject(dataList);
            byte[] data = arrayOutputStream.toByteArray();
            objectOutput.close();
            arrayOutputStream.close();
            FileOutputStream fos = context.openFileOutput(apiKey + description, Context.MODE_PRIVATE);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> loadData(Class<T> clazz) {
        try {
            Identifier identifier = (Identifier) clazz.newInstance();
            String description = identifier.getModelUrl();
            FileInputStream fis = context.openFileInput(apiKey + description);
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(b)) != -1) {
                arrayOutputStream.write(b, 0, bytesRead);
            }
            ByteArrayInputStream byteArray = new ByteArrayInputStream(arrayOutputStream.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteArray);
            List<T> dataList = (List<T>) in.readObject();
            fis.close();
            arrayOutputStream.close();
            byteArray.close();
            in.close();
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}

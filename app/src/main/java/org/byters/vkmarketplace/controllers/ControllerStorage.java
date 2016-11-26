package org.byters.vkmarketplace.controllers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ControllerStorage {

    public static final String ITEMS_INFO = "cache_items";
    public static final String TOKEN = "token_cache";
    public static final String CART_CACHE = "cart_cache";
    public static final String FAVORITES_CACHE = "favorites_cache";
    public static final String NEWS_CACHE = "cache_news";
    public static final String USERINFO_CACHE = "cache_userinfo";
    public static final String ALBUMS_CACHE = "cache_albums";
    public static final String ORDERS_HISTORY_CACHE = "orders_cache";
    private static final String TAG = "controllerStorage";
    private static ControllerStorage instance;
    private Context context;

    private ControllerStorage() {
    }

    public static ControllerStorage getInstance() {
        if (instance == null) instance = new ControllerStorage();
        return instance;
    }

    public synchronized void writeObjectToFile(Object object, String filename) {
        if (context == null) return;
        ObjectOutputStream objectOut = null;
        if (object == null)
            context.deleteFile(filename);
        else {
            try {
                FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);
                objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(object);
                fileOut.getFD().sync();

            } catch (IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            } finally {
                if (objectOut != null) {
                    try {
                        objectOut.close();
                    } catch (IOException e) {
                        Log.e(TAG, Log.getStackTraceString(e));
                    }
                }
            }
        }
    }

    @Nullable
    public synchronized Object readObjectFromFile(String filename) {
        if (context == null) return null;

        ObjectInputStream objectIn = null;
        Object object = null;
        boolean needRemove = false;
        try {

            FileInputStream fileIn = context.getApplicationContext().openFileInput(filename);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject();

        } catch (FileNotFoundException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            needRemove = true;
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        }

        if (needRemove) RemoveFile(filename);

        return object;
    }

    public synchronized void RemoveFile(String filename) {
        if (context == null) return;
        try {
            context.getApplicationContext().deleteFile(filename);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }


}

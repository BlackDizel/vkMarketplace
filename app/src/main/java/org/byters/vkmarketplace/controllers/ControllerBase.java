package org.byters.vkmarketplace.controllers;

import org.byters.vkmarketplace.controllers.utils.OnResultBase;

import java.util.ArrayList;

public class ControllerBase {
    private ArrayList<OnResultBase> listeners;

    public void addListener(OnResultBase listener) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public void removeListener(OnResultBase listener) {
        if (listeners == null) return;
        listeners.remove(listener);
    }

    public void notifyListeners(boolean isSuccess) {
        if (listeners == null) return;
        for (OnResultBase listener : listeners)
            listener.onResult(isSuccess);
    }
}

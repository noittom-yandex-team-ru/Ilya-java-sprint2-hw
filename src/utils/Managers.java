package utils;


import managers.AppManager;
import managers.InMemoryAppManager;

public class Managers {
    public static AppManager getDefault() {
        return new InMemoryAppManager();
    }
}

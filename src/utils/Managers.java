package utils;


import managers.FileBackedAppManager;
import managers.InMemoryAppManager;

import java.nio.file.Path;

public class Managers {
    public static InMemoryAppManager getDefault() {
        return new InMemoryAppManager();
    }

    public static FileBackedAppManager getFileBacked(Path path) {
        return FileBackedAppManager.getInstance(path);
    }
}

package utils;

import managers.InMemoryTasksManager;

public class Managers {
    public static InMemoryTasksManager getDefault() {
        return InMemoryTasksManager.createTaskManager();
    }
}

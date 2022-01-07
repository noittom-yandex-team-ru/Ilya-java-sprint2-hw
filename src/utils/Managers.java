package utils;

import managers.InMemoryTasksManager;
import managers.TasksManager;

public class Managers {
    public static TasksManager getDefault() {
        return InMemoryTasksManager.createTaskManager();
    }
}

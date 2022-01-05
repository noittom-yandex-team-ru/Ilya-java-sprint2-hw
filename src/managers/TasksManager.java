package managers;

import tasks.Epic;
import tasks.Story;
import tasks.Task;

import java.util.Collection;

public interface TasksManager {

    Collection<Task> findAllTasks();
    Task findTask(String id);
    boolean addTask(Task task);
    boolean updateTask(Task task);
    void deleteTask(String id);
    void deleteTasks();

    Collection<Epic> findAllEpics();
    Epic findEpic(String id);
    boolean addEpic(Epic epic);
    boolean updateEpic(Epic epic);
    void deleteEpic(String id);
    void deleteEpics();

    Collection<Story> findAllStories(String id);
    boolean addStory(Story story);
    boolean updateStory(Story story);
    void deleteStory(String id, Epic epic);
    void deleteStories(String epicId);

}

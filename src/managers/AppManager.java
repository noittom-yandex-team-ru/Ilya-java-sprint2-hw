package managers;

import models.tasks.AbstractTask;
import models.tasks.Epic;
import models.tasks.Story;
import models.tasks.Task;
import repositories.tasks.AbstractTasksRepository;

import java.util.Collection;

public interface AppManager {
    Collection<Task> findAllTasks();

    Task findTask(long id);

    Task addTask(Task task);

    Task updateTask(long id, Task task);

    Task deleteTask(long id);

    void deleteAllTasks();


    Collection<Epic> findAllEpics();

    Epic findEpic(long id);

    Epic addEpic(Epic epic);

    Epic updateEpic(long id, Epic epic);

    Epic deleteEpic(long id);

    void deleteAllEpics();


    Collection<Story> findAllStories(Epic epic);

    Story findStory(long id);

    Story addStory(Story story);

    Story updateStory(long id, Story story);

    Story deleteStory(long id);

    void deleteAllStories(Epic epic);

    <T extends AbstractTask> void createRepository(Collection<T> abstractTasks,
                                                   Class<? extends AbstractTasksRepository<T>> tasksRepositoryClass);

    long size();
}

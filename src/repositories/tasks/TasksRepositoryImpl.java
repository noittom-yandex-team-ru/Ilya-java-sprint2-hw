package repositories.tasks;

import tasks.Task;

import java.util.*;

public class TasksRepositoryImpl extends AbstractTasksRepository<Task> {

    private final Map<Long, Task> idTaskMap;

    public TasksRepositoryImpl() {
        idTaskMap = new HashMap<>();
    }

    public TasksRepositoryImpl(Collection<Task> tasks) {
        this();
        Objects.requireNonNull(tasks, "tasks must not be null");
        for (Task task : tasks) {
            idTaskMap.put(++counter, Task.createTask(counter, task));
        }
    }

    @Override
    public Collection<Task> findAll() {
        return idTaskMap.values();
    }

    @Override
    public Task find(long id) {
        return idTaskMap.get(id);
    }

    @Override
    public Task add(Task task) {
        return idTaskMap.put(++counter, Task.createTask(counter, task));
    }

    @Override
    public Task update(long id, Task task) {
        Task currentTask = idTaskMap.get(id);
        if (currentTask != null) {
            return currentTask.setTask(task);
        }
        return null;
    }

    @Override
    public Task delete(long id) {
        return idTaskMap.remove(id);
    }

    @Override
    public void clear() {
        idTaskMap.clear();
    }

    @Override
    public int size() {
        return idTaskMap.size();
    }
}

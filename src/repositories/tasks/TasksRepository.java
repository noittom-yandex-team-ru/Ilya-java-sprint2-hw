package repositories.tasks;

import models.tasks.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TasksRepository extends AbstractTasksRepository<Task> {

    private final Map<Long, Task> idTaskMap;

    public TasksRepository() {
        idTaskMap = new HashMap<>();
    }

    public TasksRepository(Collection<Task> tasks) {
        this();
        Objects.requireNonNull(tasks, "tasks must not be null");
        for (Task task : tasks) {
            idTaskMap.put(counter.increment(), Task.createTask(counter.getValue(), task));
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
        return idTaskMap.put(counter.increment(), Task.createTask(counter.getValue(), task));
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

    public Map<Long, Task> getIdTaskMap() {
        return idTaskMap;
    }

    public boolean isEmpty() {
        return idTaskMap.isEmpty();
    }
}

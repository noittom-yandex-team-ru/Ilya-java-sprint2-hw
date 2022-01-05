package managers;

import tasks.Epic;
import tasks.Story;
import tasks.Task;

import java.util.*;


public class InMemoryTasksManager implements TasksManager {

    private final Map<String, Task> idTaskMap;
    private final Map<String, Epic> idEpicMap;

    private InMemoryTasksManager(Map<String, Task> idTaskMap, Map<String, Epic> idEpicMap) {
        this.idTaskMap = Objects.requireNonNull(idTaskMap, "idTaskMap must not be null");
        this.idEpicMap = Objects.requireNonNull(idEpicMap, "idEpicMap must not be null");
    }

    private InMemoryTasksManager(List<Task> tasks, List<Epic> epics) {
        Objects.requireNonNull(tasks, "tasks must not be null");
        Objects.requireNonNull(epics, "epics must not be null");
        idTaskMap = new HashMap<>();
        for (Task task : tasks) {
            idTaskMap.put(task.getId(), task);
        }
        idEpicMap = new HashMap<>();
        for (Epic epic : epics) {
            idEpicMap.put(epic.getId(), epic);
        }
    }

    public static InMemoryTasksManager createTaskManager(Map<String, Task> idTaskMap,
                                                         Map<String, Epic> idEpicMap) {
        return new InMemoryTasksManager(idTaskMap, idEpicMap);
    }

    public static InMemoryTasksManager createTaskManager(List<Task> tasks, List<Epic> epics) {
        return new InMemoryTasksManager(tasks, epics);
    }

    @Override
    public Collection<Task> findAllTasks() {
        return idTaskMap.values();
    }

    @Override
    public Collection<Epic> findAllEpics() {
        return idEpicMap.values();
    }

    @Override
    public Collection<Story> findAllStories(String id) {
        return idEpicMap.get(id).getStories();
    }

    @Override
    public Task findTask(String id) {
        for (Task task : idTaskMap.values()) {
            if (id.equals(task.getId())) return task;
        }
        return null;
    }

    @Override
    public Epic findEpic(String id) {
        for (Epic epic : idEpicMap.values()) {
            if (id.equals(epic.getId())) return epic;
        }
        return null;
    }

    @Override
    public boolean addTask(Task task) {
        String id = task.getId();
        if (idTaskMap.containsKey(id)) {
            return false;
        } else {
            idTaskMap.put(id, task);
            return true;
        }
    }

    @Override
    public boolean addEpic(Epic epic) {
        String id = epic.getId();
        if (idEpicMap.containsKey(id)) {
            return false;
        } else {
            idEpicMap.put(id, epic);
            return true;
        }
    }

    @Override
    public boolean addStory(Story story) {
        return story.getEpic().addStory(story);
    }

    @Override
    public boolean updateTask(Task task) {
        return idTaskMap.get(task.getId()).setTask(task);
    }

    @Override
    public boolean updateEpic(Epic epic) {
        return idEpicMap.get(epic.getId()).setEpic(epic);
    }

    @Override
    public boolean updateStory(Story story) {
        Story inStory = idEpicMap.get(story.getEpic().getId()).getStory(story.getId());
        if (inStory != null) {
            return inStory.setStory(story);
        }
        return false;
    }

    @Override
    public void deleteTask(String id) {
        idTaskMap.remove(id);
    }

    @Override
    public void deleteEpic(String id) {
        idEpicMap.remove(id);
    }

    @Override
    public void deleteStory(String id, Epic epic) {
        idEpicMap.get(epic.getId()).deleteStory(id);
    }

    @Override
    public void deleteTasks() {
        idTaskMap.clear();
    }

    @Override
    public void deleteEpics() {
        idEpicMap.clear();
    }

    @Override
    public void deleteStories(String epicId) {
        idEpicMap.get(epicId).deleteAllStories();
    }

    public Map<String, Task> getIdTaskMap() {
        return idTaskMap;
    }

    public Map<String, Epic> getIdEpicMap() {
        return idEpicMap;
    }
}

package managers;

import tasks.AbstractTask;
import tasks.Epic;
import tasks.Story;
import tasks.Task;

import java.util.*;

public class InMemoryTasksManager implements TasksManager {

    private Map<String, Task> idTaskMap;
    private Map<String, Epic> idEpicMap;

    private final List<AbstractTask> tasksBuffer = new LinkedList<>();

    public InMemoryTasksManager() {
        this.idTaskMap = new HashMap<>();
        this.idEpicMap = new HashMap<>();
    }

    public InMemoryTasksManager(Map<String, Task> idTaskMap, Map<String, Epic> idEpicMap) {
        this.idTaskMap = Objects.requireNonNull(idTaskMap, "idTaskMap must not be null");
        this.idEpicMap = Objects.requireNonNull(idEpicMap, "idEpicMap must not be null");
    }

    public InMemoryTasksManager(List<Task> tasks, List<Epic> epics) {
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
    public Story findStory(Epic epic, String id) {
        if (epic != null) {
            Story story = epic.getStory(id);
            addToHistory(story);
            return story;
        }
        return null;
    }

    @Override
    public Task findTask(String id) {
        for (Task task : idTaskMap.values()) {
            if (id.equals(task.getId())) {
                addToHistory(task);
                return task;
            }
        }
        return null;
    }

    @Override
    public Epic findEpic(String id) {
        for (Epic epic : idEpicMap.values()) {
            if (id.equals(epic.getId())) {
                addToHistory(epic);
                return epic;
            }
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

    public List<AbstractTask> getHistory() {
        return tasksBuffer;
    }

    private void addToHistory(AbstractTask abstractTask) {
        if (abstractTask != null) {
            if (tasksBuffer.size() == 10) {
                tasksBuffer.remove(0);
                tasksBuffer.add(abstractTask);
                return;
            }
            tasksBuffer.add(abstractTask);
        }
    }

    public Map<String, Task> getIdTaskMap() {
        return idTaskMap;
    }

    public Map<String, Epic> getIdEpicMap() {
        return idEpicMap;
    }

    public void setIdTaskMap(Map<String, Task> idTaskMap) {
        this.idTaskMap = idTaskMap;
    }

    public void setIdTaskMap(List<Task> tasks) {
        for (Task task : tasks) {
            idTaskMap.put(task.getId(), task);
        }
    }

    public void setIdEpicMap(Map<String, Epic> idEpicMap) {
        this.idEpicMap = idEpicMap;
    }

    public void setIdEpicMap(List<Epic> epics) {
        for (Epic epic : epics) {
            idEpicMap.put(epic.getId(), epic);
        }
    }
}

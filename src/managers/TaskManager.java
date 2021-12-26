package managers;

import tasks.Epic;
import tasks.Story;
import tasks.Task;

import java.util.Collection;
import java.util.Map;


public class TaskManager {

    private final Map<String, Task> idTaskMap;
    private final Map<String, Epic> idEpicMap;

    private TaskManager(Map<String, Task> idTaskMap, Map<String, Epic> idEpicMap) {
        this.idTaskMap = idTaskMap;
        this.idEpicMap = idEpicMap;
    }

    public static TaskManager createTaskManager(Map<String, Task> idTaskMap,
                                                Map<String, Epic> idEpicMap) {
        return new TaskManager(idTaskMap, idEpicMap);
    }

    public Collection<Task> findAllTasks() {
        return idTaskMap.values();
    }

    public Collection<Epic> findAllEpics() {
        return idEpicMap.values();
    }

    public Collection<Story> findAllStories(Epic exEpic) {
        for (Epic inEpic : idEpicMap.values()) {
            if (exEpic.equals(inEpic)) {
                return inEpic.getStories();
            }
        }
        return null;
    }

    public Task findTask(String id) {
        for (Task task : idTaskMap.values()) {
            if (id.equals(task.getId())) return task;
        }
        return null;
    }

    public Epic findEpic(String id) {
        for (Epic epic : idEpicMap.values()) {
            if (id.equals(epic.getId())) return epic;
        }
        return null;
    }

    public boolean addTask(Task task) {
        String id = task.getId();
        if (idTaskMap.containsKey(id)) {
            return false;
        } else {
            idTaskMap.put(id, task);
            return true;
        }
    }

    public boolean addEpic(Epic epic) {
        String id = epic.getId();
        if (idEpicMap.containsKey(id)) {
            return false;
        } else {
            idEpicMap.put(id, epic);
            return true;
        }
    }

    public boolean addStory(Story exStory, Epic epic) {
        String storyId = exStory.getId();
        Collection<Story> stories = epic.getStories();

        if (stories.isEmpty()) {
            epic.addStory(exStory);
            return true;
        }

        for (Story inStory : stories) {
            if (storyId.equals(inStory.getId())) return false;
            else {
                epic.addStory(exStory);
                return true;
            }
        }
        return false;
    }

    public boolean updateTask(Task task) {
        return idTaskMap.get(task.getId()).setTask(task);
    }

    public boolean updateEpic(Epic epic) {
        return idEpicMap.get(epic.getId()).setEpic(epic);
    }

    public boolean updateStory(Story story) {
        Story inStory = idEpicMap.get(story.getEpic().getId()).getStory(story.getId());
        if (inStory != null) {
            return inStory.setStory(story);
        }
        return false;
    }

    public void deleteTask(String id) {
        idTaskMap.remove(id);
    }

    public void deleteEpic(String id) {
        idEpicMap.remove(id);
    }

    public void deleteStory(String id, Epic epic) {
        Story inStory = idEpicMap.get(epic.getId()).getStory(id);
        idEpicMap.remove(id);
    }

    public void deleteTasks() {
        idTaskMap.clear();
    }

    public void deleteEpics() {
        idEpicMap.clear();
    }

    public void deleteStories(String epicId) {
        idEpicMap.get(epicId).deleteAllStories();
    }
}

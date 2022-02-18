package managers;

import managers.history.InMemoryHistoryManager;
import repositories.tasks.EpicsRepository;
import repositories.tasks.TasksRepositoryImpl;
import models.tasks.Epic;
import models.tasks.Story;
import models.tasks.Task;

import java.util.Collection;
import java.util.Objects;

public class InMemoryAppManager implements AppManager {

    private TasksRepositoryImpl tasksRepository;
    private EpicsRepository epicsRepository;
    private InMemoryHistoryManager historyManager;

    public InMemoryAppManager() {
        tasksRepository = new TasksRepositoryImpl();
        epicsRepository = new EpicsRepository();
        historyManager = new InMemoryHistoryManager();
    }

    public InMemoryAppManager(TasksRepositoryImpl tasksRepository, EpicsRepository epicsRepository) {
        Objects.requireNonNull(tasksRepository, "tasksRepository must not be null");
        Objects.requireNonNull(epicsRepository, "epicsRepository must not be null");
        this.tasksRepository = tasksRepository;
        this.epicsRepository = epicsRepository;
        this.historyManager = new InMemoryHistoryManager();
    }

    public InMemoryAppManager(EpicsRepository epicsRepository, TasksRepositoryImpl tasksRepository) {
        Objects.requireNonNull(epicsRepository, "epicsRepository must not be null");
        Objects.requireNonNull(tasksRepository, "tasksRepository must not be null");
        this.epicsRepository = epicsRepository;
        this.tasksRepository = tasksRepository;
        this.historyManager = new InMemoryHistoryManager();
    }

    @Override
    public Collection<Task> findAllTasks() {
        return tasksRepository.findAll();
    }

    @Override
    public Task findTask(long id) {
        Task task = tasksRepository.find(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Task addTask(Task task) {
        return tasksRepository.add(task);
    }

    @Override
    public Task updateTask(long id, Task task) {
        return tasksRepository.update(id, task);
    }

    @Override
    public Task deleteTask(long id) {
        historyManager.remove(id);
        return tasksRepository.delete(id);
    }

    @Override
    public void deleteAllTasks() {
        tasksRepository.clear();
    }

    @Override
    public Collection<Epic> findAllEpics() {
        return epicsRepository.findAll();
    }

    @Override
    public Epic findEpic(long id) {
        Epic epic = epicsRepository.find(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Epic addEpic(Epic epic) {
        return epicsRepository.add(epic);
    }

    @Override
    public Epic updateEpic(long id, Epic epic) {
        return epicsRepository.update(id, epic);
    }

    @Override
    public Epic deleteEpic(long id) {
        Epic epic = epicsRepository.delete(id);
        if (epic != null) {
            for (Story story : epic.getStories()) {
                historyManager.remove(story.getId());
            }
            historyManager.remove(id);
        }
        return epic;
    }

    @Override
    public void deleteAllEpics() {
        epicsRepository.clear();
    }

    @Override
    public Collection<Story> findAllStories(Epic epic) {
        return epicsRepository.findAllStories(epic);
    }

    @Override
    public Story findStory(long id) {
        Story story = epicsRepository.findStory(id);
        historyManager.add(story);
        return story;
    }

    @Override
    public Story addStory(Story story) {
        return epicsRepository.addStory(story, epicsRepository.find(story.getEpic().getId()));
    }

    @Override
    public Story updateStory(long id, Story story) {
        return epicsRepository.updateStory(id, story);
    }

    @Override
    public Story deleteStory(long id) {
        historyManager.remove(id);
        return epicsRepository.deleteStory(id);
    }

    @Override
    public void deleteAllStories(Epic epic) {
        epicsRepository.clearStories(epic);
    }

    public void createTasksRepository(Collection<Task> tasks) {
        tasksRepository = new TasksRepositoryImpl(tasks);
    }

    public void createEpicsRepository(Collection<Epic> epics) {
        epicsRepository = new EpicsRepository(epics);
    }

    public TasksRepositoryImpl getTasksRepository() {
        return tasksRepository;
    }

    public EpicsRepository getEpicsRepository() {
        return epicsRepository;
    }

    public InMemoryHistoryManager getHistoryManager() {
        return historyManager;
    }
}

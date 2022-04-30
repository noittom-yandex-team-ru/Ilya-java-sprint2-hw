package managers;

import exceptions.ManagerWrongIdException;
import managers.history.InMemoryHistoryManager;
import models.enums.TypeTask;
import models.tasks.AbstractTask;
import models.tasks.Epic;
import models.tasks.Story;
import models.tasks.Task;
import repositories.tasks.AbstractTasksRepository;
import repositories.tasks.EpicsRepository;
import repositories.tasks.TasksRepository;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryAppManager implements AppManager {

    protected TasksRepository tasksRepository;
    protected EpicsRepository epicsRepository;
    protected InMemoryHistoryManager historyManager;
    protected TreeSet<AbstractTask> tasksSortedByStartTime;

    public InMemoryAppManager() {
        tasksRepository = new TasksRepository();
        epicsRepository = new EpicsRepository();
        historyManager = new InMemoryHistoryManager();
        tasksSortedByStartTime = new TreeSet<>(
                Comparator.comparing(AbstractTask::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparingLong(AbstractTask::getId)
        );
    }

    public Collection<Task> findAllTasks() {
        return tasksRepository.findAll();
    }

    @Override
    public Task findTask(long id) {
        final Task task = tasksRepository.find(id);
        if (task == null) throw new ManagerWrongIdException(TypeTask.TASK, id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Task addTask(Task task) {
        if (checkIntersection(task)) return null;
        final Task result = tasksRepository.add(task);
        addToPrioritizedListTasks(result);
        return result;
    }

    @Override
    public Task updateTask(long id, Task task) {
        if (checkIntersection(task)) return null;
        final Task result = tasksRepository.update(id, task);
        if (result == null) throw new ManagerWrongIdException(TypeTask.TASK, id);
        return result;
    }

    @Override
    public Task deleteTask(long id) {
        final Task task = tasksRepository.delete(id);
        if (task == null) throw new ManagerWrongIdException(TypeTask.TASK, id);
        historyManager.remove(id);
        tasksSortedByStartTime.remove(task);
        return task;
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasksRepository.findAll()) {
            historyManager.remove(task.getId());
            tasksSortedByStartTime.remove(task);
        }
        tasksRepository.clear();
    }

    @Override
    public Collection<Epic> findAllEpics() {
        return epicsRepository.findAll();
    }

    @Override
    public Epic findEpic(long id) {
        Epic epic = epicsRepository.find(id);
        if (epic == null) throw new ManagerWrongIdException(TypeTask.EPIC, id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Epic addEpic(Epic epic) {
        return epicsRepository.add(epic);
    }

    @Override
    public Epic updateEpic(long id, Epic epic) {
        final Epic result = epicsRepository.update(id, epic);
        if (result == null) throw new ManagerWrongIdException(TypeTask.EPIC, id);
        return result;
    }

    @Override
    public Epic deleteEpic(long id) {
        final Epic epic = epicsRepository.delete(id);
        if (epic == null) throw new ManagerWrongIdException(TypeTask.EPIC, id);
        for (Story story : epic.getStories()) {
            deleteStory(story.getId());
        }
        historyManager.remove(id);
        return epic;
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epicsRepository.findAll()) {
            for (Story story : epic.getStories()) {
                historyManager.remove(story.getId());
                tasksSortedByStartTime.remove(story);
            }
            historyManager.remove(epic.getId());
        }
        epicsRepository.clear();
    }

    @Override
    public Collection<Story> findAllStories(Epic epic) {
        return epicsRepository.findAllStories(epic);
    }

    @Override
    public Story findStory(long id) {
        Story story = epicsRepository.findStory(id);
        if (story == null) throw new ManagerWrongIdException(TypeTask.STORY, id);
        historyManager.add(story);
        return story;
    }

    @Override
    public Story addStory(Story story) {
        if (checkIntersection(story)) return null;
        final Story result = epicsRepository.addStory(story, epicsRepository.find(story.getEpic().getId()));
        addToPrioritizedListStories(result);
        return result;
    }

    @Override
    public Story updateStory(long id, Story story) {
        if (checkIntersection(story)) return null;
        final Story result = epicsRepository.updateStory(id, story);
        if (result == null) throw new ManagerWrongIdException(TypeTask.STORY, id);
        return result;
    }

    @Override
    public Story deleteStory(long id) {
        final Story story = epicsRepository.deleteStory(id);
        if (story == null) throw new ManagerWrongIdException(TypeTask.STORY, id);
        tasksSortedByStartTime.remove(story);
        historyManager.remove(id);
        return story;
    }

    @Override
    public void deleteAllStories(Epic epic) {
        for (Story story : epic.getStories()) {
            historyManager.remove(story.getId());
            tasksSortedByStartTime.remove(story);
        }
        epicsRepository.clearStories(epic);
    }

    @Override
    public <T extends AbstractTask> void createRepository(Collection<T> abstractTasks,
                                                          Class<? extends AbstractTasksRepository<T>> tasksRepositoryClass) {
        if (EpicsRepository.class.equals(tasksRepositoryClass)) {
            epicsRepository = new EpicsRepository();
            for (T epic : abstractTasks) addEpic((Epic) epic);
        } else if (TasksRepository.class.equals(tasksRepositoryClass)) {
            tasksRepository = new TasksRepository();
            for (T task : abstractTasks) addTask((Task) task);
        } else {
            throw new IllegalArgumentException("Репозиотрия с таким именем класса " + tasksRepositoryClass + " не существует");
        }
    }

    @Override
    public long size() {
        long numberAllStories = 0;
        for (Epic epic : epicsRepository.findAll()) {
            numberAllStories += epic.getStories().size();
        }
        return epicsRepository.size() + numberAllStories + tasksRepository.size();
    }

    public List<AbstractTask> getPrioritizedTasks() {
        return new ArrayList<>(tasksSortedByStartTime);
    }


    private void addToPrioritizedListTasks(Task task) {
        tasksSortedByStartTime.add(task);
    }

    private void addToPrioritizedListStories(Story story) {
        tasksSortedByStartTime.add(story);
    }

    private boolean checkIntersection(AbstractTask checkedTask) {
        final TreeSet<AbstractTask> prioritizedTasks = tasksSortedByStartTime;
        if (prioritizedTasks.size() > 0) {
            final LocalDateTime startTimeCheckedTask = checkedTask.getStartTime();
            final LocalDateTime endTimeCheckedTask = checkedTask.getEndTime();
            if (startTimeCheckedTask != null) {
                for (AbstractTask prioritizedTask : prioritizedTasks) {
                    final LocalDateTime startTimePrioritizedTask = prioritizedTask.getStartTime();
                    final LocalDateTime endTimePrioritizedTask = prioritizedTask.getEndTime();
                    if (startTimePrioritizedTask != null && intersected(startTimeCheckedTask, endTimeCheckedTask,
                            startTimePrioritizedTask, endTimePrioritizedTask)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean intersected(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        if (start1.isAfter(start2) && start1.isBefore(end2)) {
            return true;
        }
        return start2.isAfter(start1) && start2.isBefore(end1);
    }

    public TasksRepository getTasksRepository() {
        return tasksRepository;
    }

    public EpicsRepository getEpicsRepository() {
        return epicsRepository;
    }

    public InMemoryHistoryManager getHistoryManager() {
        return historyManager;
    }
}

package managers;

import exceptions.ManagerSaveException;
import managers.history.HistoryManager;
import models.enums.StateTask;
import models.enums.TypeTask;
import models.tasks.AbstractTask;
import models.tasks.Epic;
import models.tasks.Story;
import models.tasks.Task;
import repositories.tasks.CombinedTasksRepository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileBackedAppManager extends InMemoryAppManager implements AppManager {
    private final Path path;
    private CombinedTasksRepository combinedTasksRepository;

    public FileBackedAppManager(Path path) {
        super();
        this.path = path;
        loadFromFile();
    }

    public void saveTasks() {
        final String HEADER = "id,type,name,status,description,epic";
        combinedTasksRepository = CombinedTasksRepository.getInstance(getEpicsRepository(), getTasksRepository());
        try (BufferedWriter fileWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.write(HEADER);
            fileWriter.write(System.lineSeparator());
            LinkedHashMap<Long, AbstractTask> tasks = combinedTasksRepository.getAbstractTasks();
            for (Map.Entry<Long, AbstractTask> task : tasks.entrySet()) {
                fileWriter.write(taskToString(task.getValue()));
            }
            fileWriter.write(System.lineSeparator());
            fileWriter.write(toString(super.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка во время автосохранения!");
        }
    }

    public void loadFromFile() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Files.createFile(path));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка во время загрузки файлы!");
        }

        if (lines.size() > 1) {
            for (int i = 1; i < lines.size() - 2; i++) {
                AbstractTask abstractTask = taskFromString(lines.get(i));
                TypeTask typeTask = abstractTask.getTypeTask();
                if (typeTask.isEpic()) {
                    super.addEpic((Epic) abstractTask);
                } else if (typeTask.isStory()) {
                    Story story = (Story) abstractTask;
                    super.addStory(story);
                } else {
                    super.addTask((Task) abstractTask);
                }
            }

            combinedTasksRepository = CombinedTasksRepository.getInstance(getEpicsRepository(), getTasksRepository());

            String historyLine = lines.get(lines.size() - 1);
            if (!historyLine.isBlank()) {
                historyFromString(historyLine);
            }
        }

    }

    @Override
    public void createTasksRepository(Collection<Task> tasks) {
        super.createTasksRepository(tasks);
        saveTasks();
    }

    @Override
    public void createEpicsRepository(Collection<Epic> epics) {
        super.createEpicsRepository(epics);
        saveTasks();
    }

    @Override
    public Task findTask(long id) {
        Task task = super.findTask(id);
        saveTasks();
        return task;
    }

    @Override
    public Task addTask(Task task) {
        Task result = super.addTask(task);
        saveTasks();
        return result;
    }

    @Override
    public Task updateTask(long id, Task task) {
        Task result = super.updateTask(id, task);
        saveTasks();
        return result;
    }

    @Override
    public Task deleteTask(long id) {
        Task task = super.deleteTask(id);
        saveTasks();
        return task;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        saveTasks();
    }

    @Override
    public Epic findEpic(long id) {
        Epic epic = super.findEpic(id);
        saveTasks();
        return epic;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic result = super.addEpic(epic);
        saveTasks();
        return result;
    }

    @Override
    public Epic updateEpic(long id, Epic epic) {
        Epic result = super.updateEpic(id, epic);
        saveTasks();
        return result;
    }

    @Override
    public Epic deleteEpic(long id) {
        Epic result = super.deleteEpic(id);
        saveTasks();
        return result;
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        saveTasks();
    }

    @Override
    public Story findStory(long id) {
        Story story = super.findStory(id);
        saveTasks();
        return story;
    }

    @Override
    public Story addStory(Story story) {
        Story result = super.addStory(story);
        saveTasks();
        return result;
    }

    @Override
    public Story updateStory(long id, Story story) {
        Story result = super.updateStory(id, story);
        saveTasks();
        return result;
    }

    @Override
    public Story deleteStory(long id) {
        Story result = super.deleteStory(id);
        saveTasks();
        return result;
    }

    @Override
    public void deleteAllStories(Epic epic) {
        super.deleteAllStories(epic);
        saveTasks();
    }

    private String toString(HistoryManager historyManager) {
        StringBuilder sb = new StringBuilder();

        if (!historyManager.getHistory().isEmpty()) {
            for (AbstractTask task : historyManager.getHistory()) {
                sb.append(task.getId()).append(",");
            }
        }

        return sb.toString();
    }

    private List<AbstractTask> historyFromString(String value) {
        String[] values = value.split(",");

        if (values.length != 0) {
            Map<Long, AbstractTask> tasks = combinedTasksRepository.getAbstractTasks();
            for (String id : values) {
                super.historyManager.add(tasks.get(Long.parseLong(id)));
            }
        }

        return super.historyManager.getHistory();
    }


    private String taskToString(AbstractTask abstractTask) {
        StringBuilder sb = new StringBuilder();
        TypeTask typeTask;
        sb.append(abstractTask.getId())
                .append(",")
                .append(typeTask = abstractTask.getTypeTask())
                .append(",")
                .append(abstractTask.getName())
                .append(",")
                .append(abstractTask.getStateTask())
                .append(",")
                .append(abstractTask.getDescription())
                .append(",");

        if (typeTask.isStory()) {
            Story story = (Story) abstractTask;
            sb.append(story.getEpic().getId());
        }
        return sb.append(System.lineSeparator()).toString();
    }

    private AbstractTask taskFromString(String value) {
        String[] split = value.trim().split(",");

        if (split.length > 6 || split.length < 5) throw new IllegalArgumentException();

        long id = Long.parseLong(split[0]);
        TypeTask typeTask = TypeTask.valueOf(split[1]);
        String name = split[2];
        StateTask stateTask = StateTask.valueOf(split[3]);
        String description = split[4];

        if (typeTask.isEpic()) {
            return Epic.createEpic(id, name, stateTask, description);
        }

        if (typeTask.isStory()) {
            long epicId = Long.parseLong(split[5]);
            return Story.createStory(id, name, description, super.findEpic(epicId), stateTask);
        }

        return Task.createTask(id, name, stateTask, description);
    }
}

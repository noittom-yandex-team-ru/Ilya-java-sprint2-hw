import managers.AppManager;
import managers.InMemoryAppManager;
import repositories.tasks.EpicsRepository;
import repositories.tasks.TasksRepositoryImpl;
import models.tasks.Epic;
import models.tasks.Story;
import models.tasks.Task;
import models.enums.StateTask;
import utils.Managers;

import java.util.List;

public class Main {

    final static int NUMBER_TASKS = 5;
    static int id_counter = 0;

    public static void main(String[] args) {

        InMemoryAppManager appManager = (InMemoryAppManager) Managers.getDefault();
        appManager.createEpicsRepository(List.of(
                Epic.createEpic("Epic1", "First epic"),
                Epic.createEpic("Epic2"),
                Epic.createEpic("Epic3")
        )); id_counter += 3;



        Epic epic1 = appManager.findEpic(1);
        Epic epic2 = appManager.findEpic(2);
        Epic epic3 = appManager.findEpic(3);

        fillInStories(appManager, epic1, NUMBER_TASKS); id_counter += 5;
        fillInStories(appManager, epic2, NUMBER_TASKS); id_counter += 5;
        fillInStories(appManager, epic3, NUMBER_TASKS); id_counter += 5;

        System.out.println("Тест №1: " + (epic1.getStories().size() == epic2.getStories().size()
                && epic2.getStories().size() == epic3.getStories().size()));

        assert epic3.getStory(id_counter) != null : "ID generation is broken";

        Story story4OfEpic2 = appManager.findStory(18);
        appManager.updateStory(story4OfEpic2.getId(),
                Story.createStory("Homework!", "Finish homework", epic3));

        System.out.println("Тест №2: " + story4OfEpic2.equals(appManager.findStory(18)));

        appManager.deleteAllStories(epic1);
        System.out.println("Тест №3: " + (epic1.getStories().size() == 0));

        appManager.addStory(Story.createStory("Homo", epic1)); id_counter++;
        System.out.println("Тест №4: " + (epic1.getStories().size() == 1));

        appManager.deleteStory(10);
        appManager.deleteStory(10);
        System.out.println("Тест №5: " + (epic2.getStories().size() == 4));

        Story newStory = Story.createStory(19, "NameStory", epic1);
        appManager.updateStory(newStory.getId(), newStory);
        System.out.println("Тест №6: " + newStory.equals(epic1.getStory(19)));

        appManager.createTasksRepository(List.of(
                Task.createTask("Task1", "First task"),
                Task.createTask("Task2"),
                Task.createTask("Task3"),
                Task.createTask("Task4"),
                Task.createTask("Task5")
        )); id_counter += 5;

        EpicsRepository epicsRepository = appManager.getEpicsRepository();
        TasksRepositoryImpl tasksRepository = appManager.getTasksRepository();

        System.out.println("Тест №7: " + (epicsRepository.size() == epicsRepository.findAll().size()
                && tasksRepository.size() == tasksRepository.findAll().size()));

        Epic newEpic = Epic.createEpic(id_counter + 1,"Epic4");
        appManager.addEpic(newEpic); id_counter++;
        System.out.println("Тест №8: " + (appManager.findAllEpics().size() == 4));

        appManager.updateEpic(id_counter, Epic.createEpic("NEW_EPIC", "DESCRIPTION FOR EPIC!"));
        System.out.println("Тест №9: " + newEpic.equals(appManager.findEpic(newEpic.getId())));

        appManager.deleteEpic(id_counter);
        System.out.println("Тест №10: " +(appManager.findAllEpics().size() == 3));

        Task newTask = Task.createTask("NEW_TASK", "DESCRIPTION FOR TASK!");
        appManager.addTask(newTask); id_counter++;
        System.out.println("Тест №11: " + (appManager.findAllTasks().size() == 6));

        newTask.setName("KILL_TASK");
        newTask.setDescription(null);
        appManager.updateTask(id_counter, newTask);
        System.out.println("Тест №12: " + newTask.getName().equals(appManager.findTask(id_counter).getName()));

        epic1.setStatusStory(19, StateTask.DONE);
        System.out.println(("Тест №13: " + StateTask.DONE.equals(epic1.getStateTask())));

        appManager.addStory(Story.createStory("ANOTHER_STORY", epic1)); id_counter++;
        System.out.println(("Тест №14: " + StateTask.IN_PROGRESS.equals(epic1.getStateTask())));

        epic1.setStatusStory(19, StateTask.NEW);
        System.out.println(("Тест №15: " + StateTask.NEW.equals(epic1.getStateTask())));


        System.out.println("Тест №16: ");
        System.out.println("Before deletion: ");
        for (int i = 0; i < 9; i++) {
            appManager.findStory(19);
        }
        appManager.findEpic(3);
        appManager.findTask(26);
        appManager.getHistoryManager().getHistory().forEach(System.out::println);

        appManager.deleteStory(19);
        appManager.deleteTask(26);
        appManager.deleteEpic(3);

        System.out.println("\nAfter deletion: ");
        appManager.getHistoryManager().getHistory().forEach(System.out::println);

        appManager.deleteTask(22);
        System.out.println(("Тест №17: " + (appManager.getTasksRepository().size() == 4)));

        appManager.deleteAllTasks();
        System.out.println(("Тест №18: " + appManager.findAllTasks().isEmpty()));

        appManager.deleteAllStories(epic1);
        System.out.println(("Тест №19: " + appManager.findAllStories(epic1).isEmpty()));

        appManager.deleteAllTasks();
        System.out.println(("Тест №20: " + appManager.findAllTasks().isEmpty()));


    }

    public static void fillInStories(AppManager manager, Epic epic, int number) {
        for (int i = 0; i < number; i++) {
            manager.addStory(Story.createStory("Story" + (i + 1), epic));
        }
    }
}

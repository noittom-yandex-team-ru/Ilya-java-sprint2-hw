import managers.TaskManager;
import tasks.Epic;
import tasks.Story;
import tasks.Task;

import java.util.List;

public class Main {

    private static final String EPIC_ID_1 = "00001";
    private static final String EPIC_ID_2 = "00002";
    private static final String EPIC_ID_3 = "00003";
    private static final String EPIC_ID_4 = "00004";

    private static final String STORY_ID_1111 = "1111";

    private static final String TASK_ID_1 = "00011";
    private static final String TASK_ID_2 = "00022";
    private static final String TASK_ID_3 = "00033";
    private static final String TASK_ID_4 = "00044";
    private static final String TASK_ID_5 = "00055";
    private static final String TASK_ID_6 = "00066";

    public static void main(String[] args) {
        List<Epic> epics = List.of(
                Epic.createEpic(EPIC_ID_1, "Epic1", "First epic"),
                Epic.createEpic(EPIC_ID_2, "Epic2"),
                Epic.createEpic(EPIC_ID_3, "Epic3")
        );

        for (Epic epic : epics) {
            for (int i = 0; i < 5; i++) {
                epic.addStory(Story.createStory("000" + (i + 1), "Story" + (i + 1), epic));
            }
        }

        System.out.println("Тест №1: " + ((epics.get(0).getStories().size() == 5)
                && (epics.get(2).getStories().size() == 5)));

        Epic epic2 = epics.get(1);
        epic2.setStories(List.of(
                Story.createStory(STORY_ID_1111, "Homework!", "Finish homework", epic2),
                Story.createStory("2222", "Cleaning!", "Clean room", epic2)
        ));

        Epic epic1 = epics.get(0);
        Epic epic3 = epics.get(2);

        System.out.println("Тест №2: " + (epic2.getStories().size() == 2));

        epic1.deleteAllStories();
        System.out.println("Тест №3: " + (epic1.getStories().size() == 0));

        epic3.deleteStory("0001");
        epic3.deleteStory("0005");
        System.out.println("Тест №4: " + (epic3.getStories().size() == 3));

        Story newStory = Story.createStory(STORY_ID_1111, "Shhhhh!", "Booom!", epic2);
        Story story2 = epic2.getStory(STORY_ID_1111);
        assert story2 != null;
        story2.setStory(newStory);

        System.out.println("Тест №5: " + newStory.equals(story2));

        List<Task> tasks = List.of(
                Task.createTask(TASK_ID_1, "Task1", "First task"),
                Task.createTask(TASK_ID_2, "Task2"),
                Task.createTask(TASK_ID_3, "Task3"),
                Task.createTask(TASK_ID_4, "Task4"),
                Task.createTask(TASK_ID_5, "Task5")
        );

        TaskManager tm = TaskManager.createTaskManager(tasks, epics);
        System.out.println("Тест №6: " + (tm.getIdEpicMap().size() == tm.findAllEpics().size()
                && tm.getIdTaskMap().size() == tm.findAllTasks().size()));

        System.out.println("Тест №7: " + tm.findEpic(EPIC_ID_1).getStories().isEmpty());

        tm.addStory(Story.createStory(STORY_ID_1111, "Homo", epic1));
        System.out.println("Тест №8: " + (epic1.getStories().size() == 1));

        Story story1111 = Story.createStory(STORY_ID_1111, "FooF", epic1);
        tm.updateStory(story1111);
        System.out.println("Тест №9: "
                + story1111.equals(tm.findEpic(epic1.getId()).getStory(STORY_ID_1111)));

        tm.deleteStory(STORY_ID_1111, epic1);
        System.out.println("Тест №10: " + (epic1.getStories().size() == 0));

        System.out.println("Тест №11: " + (tm.findAllStories(EPIC_ID_2).size() == 2));

        System.out.println("Тест №12: " + (tm.findAllStories(EPIC_ID_2).size() == 2));

        Epic newEpic = Epic.createEpic(EPIC_ID_4, "Epic4");
        tm.addEpic(newEpic);
        System.out.println("Тест №13: " + (tm.findAllEpics().size() == 4));

        newEpic.setEpic(Epic.createEpic(EPIC_ID_4, "NEW_EPIC", "DESCRIPTION FOR EPIC!"));
        tm.updateEpic(newEpic);
        System.out.println("Тест №14: " + newEpic.equals(tm.findEpic(EPIC_ID_4)));

        tm.deleteEpic(EPIC_ID_4);
        System.out.println("Тест №15: " +(tm.findAllEpics().size() == 3));

        Task newTask = Task.createTask(TASK_ID_6, "NEW_TASK", "DESCRIPTION FOR TASK!");
        tm.addTask(newTask);
        System.out.println("Тест №16: " + (tm.findAllTasks().size() == 6));

        newTask.setName("KILL_TASK");
        newTask.setDescription(null);
        tm.updateTask(newTask);
        System.out.println(("Тест №17: " + newTask.equals(tm.findTask(TASK_ID_6))));

        tm.deleteTask(TASK_ID_1);
        System.out.println(("Тест №18: " + (tm.findAllTasks().size() == 5)));

        tm.deleteStories(EPIC_ID_1);
        System.out.println(("Тест №19: " + tm.findAllStories(EPIC_ID_1).isEmpty()));

        tm.deleteEpics();
        System.out.println(("Тест №20: " + tm.findAllEpics().isEmpty()));

        tm.deleteTasks();
        System.out.println(("Тест №21: " + tm.findAllTasks().isEmpty()));
    }
}

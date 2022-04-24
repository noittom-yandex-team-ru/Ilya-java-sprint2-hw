package managers;

import managers.history.HistoryManager;
import models.enums.StateTask;
import models.enums.TypeTask;
import models.tasks.AbstractTask;
import models.tasks.Epic;
import models.tasks.Story;
import models.tasks.Task;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import repositories.tasks.AbstractTasksRepository;
import utils.Managers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedAppManagerTest extends AppManagerTest<FileBackedAppManager> {
    protected static final String FILE_NAME_FOR_GENERALIZED_TEST = "test1";
    private static final String FILE_NAME_FOR_FILE_BACKED_TEST = "test2";

    private FileBackedAppManagerTest() {
        super(Managers.getFileBacked(Path.of(FILE_NAME_FOR_GENERALIZED_TEST)));
    }

    // All tests are below check the state of FileBackedAppManager loading data from the file
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class NestedBackedAppManagerTest {
        private FileBackedAppManager fileBackedAppManager;

        @BeforeEach
        void setUp() {
            AbstractTasksRepository.TASK_COUNTER.reset();
            fileBackedAppManager = Managers.getFileBacked(Path.of(FILE_NAME_FOR_FILE_BACKED_TEST));
        }

        @AfterEach
        void tearDown() throws IOException {
            final Path pathDataFile = Path.of(FILE_NAME_FOR_FILE_BACKED_TEST);
            if (Files.exists(pathDataFile)) Files.delete(pathDataFile);
        }

        private void add(TypeTask typeTask, String name, Epic epic) {
            switch (typeTask) {
                case TASK:
                    fileBackedAppManager.addTask(Task.createTask(name));
                    break;
                case EPIC:
                    fileBackedAppManager.addEpic(Epic.createEpic(name));
                    break;
                case STORY:
                    fileBackedAppManager.addStory(Story.createStory(name, epic));
                    break;
            }
        }


        private void add(TypeTask typeTask, String name) {
            add(typeTask, name, null);
        }

        @Test
        void findTask() {
            add(TypeTask.TASK, "Task");
            final Task task = fileBackedAppManager.findTask(1);
            fileBackedAppManager.load();
            assertEquals(1, task.getId());
            assertEquals("Task", task.getName());
            assertEquals(TypeTask.TASK, task.getTypeTask());
            assertEquals(StateTask.NEW, task.getStateTask());
            assertTrue(task.getDescription().isEmpty());
            assertEquals(1, fileBackedAppManager.getTasksRepository().size());

        }

        @Test
        void updateTask() {
            add(TypeTask.TASK, "Task");
            final Task task = fileBackedAppManager.updateTask(1,
                    Task.createTask(3, "UpdatedTaskName", "Updated task!", StateTask.IN_PROGRESS));
            fileBackedAppManager.load();
            final Task updatedTask = fileBackedAppManager.findTask(1);
            assertEquals(task, updatedTask);
            assertEquals("UpdatedTaskName", updatedTask.getName());
            assertEquals(StateTask.IN_PROGRESS, updatedTask.getStateTask());
            assertEquals("Updated task!", updatedTask.getDescription());
        }


        @Test
        void deleteTask() {
            add(TypeTask.TASK, "Task");
            fileBackedAppManager.deleteTask(1);
            fileBackedAppManager.load();
            assertTrue(fileBackedAppManager.getTasksRepository().isEmpty());
        }


        @Test
        void deleteAllTasks() {
            add(TypeTask.TASK, "Task");
            add(TypeTask.TASK, "Task");
            add(TypeTask.TASK, "Task");
            fileBackedAppManager.deleteAllTasks();
            fileBackedAppManager.load();
            assertTrue(fileBackedAppManager.getTasksRepository().isEmpty());
        }

        @Test
        void findEpic() {
            add(TypeTask.EPIC, "Epic");
            final Epic epic = fileBackedAppManager.findEpic(1);
            fileBackedAppManager.load();
            assertEquals(1, epic.getId());
            assertEquals("Epic", epic.getName());
            assertEquals(TypeTask.EPIC, epic.getTypeTask());
            assertEquals(StateTask.NEW, epic.getStateTask());
            assertEquals(0, epic.getStories().size());
            assertTrue(epic.getDescription().isEmpty());
            assertEquals(1, fileBackedAppManager.getEpicsRepository().size());
        }

        @Test
        void updateEpic() {
            add(TypeTask.EPIC, "Epic");
            final Epic epic = fileBackedAppManager.updateEpic(1,
                    Epic.createEpic(5, "UpdatedEpicName", StateTask.IN_PROGRESS, "Updated epic!"));
            final Epic updatedEpic = fileBackedAppManager.findEpic(1);
            fileBackedAppManager.load();
            assertEquals(epic, updatedEpic);
            assertEquals("UpdatedEpicName", updatedEpic.getName());
            assertEquals(StateTask.NEW, updatedEpic.getStateTask()); // Because the stateTask for epics is calculated automatically
            assertEquals("Updated epic!", updatedEpic.getDescription());
        }

        @Test
        void deleteEpic() {
            add(TypeTask.EPIC, "Epic");
            final Epic epic = fileBackedAppManager.deleteEpic(1);
            fileBackedAppManager.load();
            assertEquals(1, epic.getId());
            assertEquals(0, fileBackedAppManager.getEpicsRepository().size());
        }

        @Test
        void deleteAllEpics() {
            add(TypeTask.EPIC, "Epic");
            fileBackedAppManager.deleteAllEpics();
            fileBackedAppManager.load();
            assertEquals(0, fileBackedAppManager.getEpicsRepository().size());
        }

        @Test
        void findStory() {
            add(TypeTask.EPIC, "Epic");
            add(TypeTask.STORY, "Story", fileBackedAppManager.findEpic(1));
            final Story story = fileBackedAppManager.findStory(2);
            fileBackedAppManager.load();
            assertEquals(2, story.getId());
            assertEquals("Story", story.getName());
            assertEquals(TypeTask.STORY, story.getTypeTask());
            assertEquals(StateTask.NEW, story.getStateTask());
        }

        @Test
        void updateStory() {
            add(TypeTask.EPIC, "Epic");
            add(TypeTask.EPIC, "Epic");
            final Epic epic1 = fileBackedAppManager.findEpic(1);
            add(TypeTask.STORY, "Story", epic1);
            final Epic epic2 = fileBackedAppManager.findEpic(2);
            fileBackedAppManager.updateStory(3,
                    Story.createStory(10, "UpdatedEpicName", "StoryOwnedEpic1", epic2, StateTask.DONE));
            final Story updatedStory = fileBackedAppManager.findStory(3);
            fileBackedAppManager.load();
            assertEquals(3, updatedStory.getId());
            assertEquals("UpdatedEpicName", updatedStory.getName());
            assertEquals("StoryOwnedEpic1", updatedStory.getDescription());
            assertEquals(epic2, updatedStory.getEpic());
            assertEquals(StateTask.DONE, updatedStory.getStateTask());
            assertEquals(epic2, epic2.getStory(3).getEpic());
            assertEquals(0, fileBackedAppManager.findEpic(1).getStories().size());
            assertEquals(1, fileBackedAppManager.findEpic(2).getStories().size());
        }


        @Test
        void deleteStory() {
            add(TypeTask.EPIC, "Epic");
            add(TypeTask.STORY, "Story", fileBackedAppManager.findEpic(1));
            final Story story = fileBackedAppManager.deleteStory(2);
            fileBackedAppManager.load();
            assertEquals(2, story.getId());
            assertEquals(0, fileBackedAppManager.findAllStories(story.getEpic()).size());
        }

        @Test
        void deleteAllStories() {
            add(TypeTask.EPIC, "Epic");
            final Epic epic = fileBackedAppManager.findEpic(1);
            add(TypeTask.STORY, "Story", epic);
            add(TypeTask.STORY, "Story", epic);
            fileBackedAppManager.deleteAllStories(epic);
            fileBackedAppManager.load();
            assertEquals(0, fileBackedAppManager.findAllStories(epic).size());
        }

        private Stream<Arguments> tasksFindSequenceDataProvider() {
            return Stream.of(
                    // findTasks, idsTasksDeleteFromHistory, expectedHistoryManagerIds
                    Arguments.of(new Object[][]{}, new long[]{}, new long[]{}),
                    Arguments.of(new Object[][]{}, new long[]{1, 2}, new long[]{}),
                    Arguments.of(new Object[][]{
                                    {5L, TypeTask.STORY},
                                    {4L, TypeTask.EPIC},
                                    {3L, TypeTask.EPIC},
                                    {2L, TypeTask.TASK},
                                    {1L, TypeTask.TASK}},
                            new long[]{},
                            new long[]{5L, 4L, 3L, 2L, 1L}),
                    Arguments.of(new Object[][]{
                                    {1L, TypeTask.TASK},
                                    {3L, TypeTask.EPIC},
                                    {3L, TypeTask.EPIC},
                                    {5L, TypeTask.STORY},
                                    {5L, TypeTask.STORY},
                                    {5L, TypeTask.STORY},
                                    {2L, TypeTask.TASK},
                                    {2L, TypeTask.TASK},
                                    {1L, TypeTask.TASK},},
                            new long[]{},
                            new long[]{3L, 5L, 2L, 1L}),
                    Arguments.of(new Object[][]{
                                    {5L, TypeTask.STORY},
                                    {4L, TypeTask.EPIC},
                                    {3L, TypeTask.EPIC},
                                    {2L, TypeTask.TASK},
                                    {1L, TypeTask.TASK}},
                            new long[]{1L, 2L, 3L, 4L, 5L},
                            new long[]{}),
                    Arguments.of(new Object[][]{
                                    {5L, TypeTask.STORY},
                                    {4L, TypeTask.EPIC},
                                    {3L, TypeTask.EPIC},
                                    {2L, TypeTask.TASK},
                                    {1L, TypeTask.TASK}},
                            new long[]{5L, 1L},
                            new long[]{4L, 3L, 2}),
                    Arguments.of(new Object[][]{
                                    {5L, TypeTask.STORY},
                                    {4L, TypeTask.EPIC},
                                    {3L, TypeTask.EPIC},
                                    {2L, TypeTask.TASK},
                                    {1L, TypeTask.TASK}},
                            new long[]{3L},
                            new long[]{5L, 4L, 2L, 1L})
            );
        }

        @ParameterizedTest
        @MethodSource("tasksFindSequenceDataProvider")
        void checkHistoryManagerState(Object[][] findTasks, long[] idsTasksDeleteFromHistory,
                                      long[] expectedHistoryManagerIds) {
            add(TypeTask.TASK, "Task1");
            add(TypeTask.TASK, "Task2");
            add(TypeTask.EPIC, "Epic1");
            add(TypeTask.EPIC, "Epic2");
            if (findTasks.length != 0) {
                fileBackedAppManager.addStory(Story.createStory("Story1", "StoryOwnedEpic1",
                        fileBackedAppManager.findEpic(3)));
            }
            findTasks(fileBackedAppManager, findTasks);
            fileBackedAppManager.load();
            final List<AbstractTask> tasksFromHistory =
                    deleteTasksFromHistory(fileBackedAppManager.getHistoryManager(), idsTasksDeleteFromHistory).getHistory();
            final long[] historyManagerIds = new long[expectedHistoryManagerIds.length];
            for (int i = 0; i < historyManagerIds.length; i++) {
                historyManagerIds[i] = tasksFromHistory.get(i).getId();
            }
            assertArrayEquals(expectedHistoryManagerIds, historyManagerIds);
        }

        private void findTasks(FileBackedAppManager appManager, Object[][] findTasks) {
            for (Object[] findTask : findTasks) {
                final long id = (long) findTask[0];
                final TypeTask typeTask = (TypeTask) findTask[1];
                switch (typeTask) {
                    case TASK:
                        appManager.findTask(id);
                        break;
                    case EPIC:
                        appManager.findEpic(id);
                        break;
                    case STORY:
                        appManager.findStory(id);
                        break;
                }
            }
        }

        private HistoryManager deleteTasksFromHistory(HistoryManager historyManager, long[] idsTasksDeleteFromHistory) {
            for (long id : idsTasksDeleteFromHistory) {
                historyManager.remove(id);
            }
            return historyManager;
        }


        private Stream<Arguments> tasksStartTimeAndDurationDataProvider() {
            // LocalDateTime, Duration, TypeTask, expectedPrioritizedTasks
            return Stream.of(
                    Arguments.of(
                            new Object[][]{
                                    {null, null, TypeTask.TASK}, // id = 1
                                    {null, null, TypeTask.STORY, 1L}, // id = 3
                            },
                            new long[]{1L, 3L}),
                    Arguments.of(
                            new Object[][]{
                                    {LocalDateTime.of(2020, Month.APRIL, 10, 12, 0, 0),
                                            Duration.ofDays(1).plusHours(1).plusMinutes(30).plusSeconds(20),
                                            TypeTask.TASK}, // id = 1
                                    {LocalDateTime.of(2020, Month.APRIL, 11, 13, 30, 21),
                                            Duration.ofDays(0).plusHours(1).plusMinutes(30).plusSeconds(0),
                                            TypeTask.STORY,
                                            1L}, // id = 3
                                    {LocalDateTime.of(2020, Month.MARCH, 25, 11, 10, 40),
                                            Duration.ofDays(1).plusHours(2).plusMinutes(30).plusSeconds(20),
                                            TypeTask.STORY,
                                            1L}, // id = 4
                                    {LocalDateTime.of(2020, Month.MARCH, 28, 20, 10, 40),
                                            Duration.ofDays(5),
                                            TypeTask.STORY,
                                            1L}, // id = 5
                            },
                            new long[]{4L, 5L, 1L, 3L}),
                    Arguments.of(new Object[][]{
                                    {LocalDateTime.of(2020, Month.APRIL, 2, 12, 0, 0),
                                            Duration.ofDays(1),
                                            TypeTask.TASK}, // id = 2
                                    {LocalDateTime.of(2020, Month.APRIL, 1, 12, 0, 0),
                                            Duration.ofDays(1),
                                            TypeTask.STORY,
                                            1L}, // id = 3
                                    {LocalDateTime.of(2020, Month.APRIL, 3, 12, 0, 0),
                                            Duration.ofDays(1),
                                            TypeTask.STORY,
                                            1L}, // id = 4
                            },
                            new long[]{3L, 1L, 4L}),
                    Arguments.of(new Object[][]{
                                    {LocalDateTime.of(2020, Month.APRIL, 2, 12, 0, 0),
                                            Duration.ofDays(1),
                                            TypeTask.STORY,
                                            1L}, // id = 2
                                    {LocalDateTime.of(2020, Month.APRIL, 1, 12, 0, 0),
                                            Duration.ofDays(1).plusNanos(1),
                                            TypeTask.STORY,
                                            1L}, // id = 3
                                    {LocalDateTime.of(2020, Month.APRIL, 2, 23, 59, 59),
                                            Duration.ofDays(1),
                                            TypeTask.STORY,
                                            1L}, // id = 4
                            },
                            new long[]{2L})
            );
        }

        @ParameterizedTest
        @MethodSource("tasksStartTimeAndDurationDataProvider")
        public void testPrioritizedTasks(Object[][] addTasks, long[] expectedPrioritizedTasksIds) {
            fileBackedAppManager.addEpic(Epic.createEpic("Epic"));
            addTasks(fileBackedAppManager, addTasks);
            // While loading from a file, the process of filling the manager, so for tasks recalculates ID
            // This is taken into account in expectedPrioritizedTasksIds array
            fileBackedAppManager.load();
            assertArrayEquals(expectedPrioritizedTasksIds, fileBackedAppManager.getPrioritizedTasks().stream()
                    .map(AbstractTask::getId)
                    .mapToLong(l -> l)
                    .toArray());
        }

        private void addTasks(FileBackedAppManager appManager, Object[][] addTasks) {
            for (Object[] addTask : addTasks) {
                final LocalDateTime startTime = (LocalDateTime) addTask[0];
                final Duration duration = (Duration) addTask[1];
                final TypeTask typeTask = (TypeTask) addTask[2];
                switch (typeTask) {
                    case TASK:
                        appManager.addTask(Task.builder("Task")
                                .startTime(startTime)
                                .duration(duration)
                                .build());
                        break;
                    case STORY:
                        final long epicId = (long) addTask[3];
                        appManager.addStory(Story.builder("Task", appManager.findEpic(epicId))
                                .startTime(startTime)
                                .duration(duration)
                                .build());
                        break;
                }
            }
        }
    }
}
package repositories.tasks;

import models.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TasksRepositoryTest {

    private static TasksRepository repository;

    private final static List<Task> tasks = List.of(
            Task.createTask("Task1"),
            Task.createTask("Task2"),
            Task.createTask("Task3")
    );

    @BeforeEach
    void initTasksRepository() {
        repository = new TasksRepository(tasks);
    }

    @Test
    void findAll() {
        final Task[] actualTasks = repository.findAll().toArray(Task[]::new);
        assertEquals(3, actualTasks.length);
        for (int i = 1; i < 4; i++) {
            Task task = actualTasks[i - 1];
            assertEquals(i, task.getId());
            assertEquals("Task" + i, task.getName());
        }
    }

    @Test
    void find() {
        final Task task = repository.find(2);
        assertEquals(2, task.getId());
        assertEquals("Task2", task.getName());
    }

    @Test
    void shouldReturnNullWhenFindTaskByNonexistentId() {
        assertNull(repository.find(100));
    }

    @Test
    void add() {
        final Task task4 = Task.createTask("Task4");
        repository.add(task4);
        assertEquals(4, repository.find(4).getId());
        assertEquals(4, repository.size());
    }

    @Test
    void update() {
        final Task task = Task.createTask(10, "TASK", "IT'S AN TASK");
        repository.update(3, task);
        Task actualTask = repository.find(3);
        assertEquals(3, actualTask.getId());
        assertEquals("TASK", actualTask.getName());
        assertEquals("IT'S AN TASK", actualTask.getDescription());
    }

    @Test
    void shouldReturnNullWhenUpdateTaskByNonexistentId() {
        assertNull(repository.update(100, Task.createTask(10, "TASK", "IT'S AN TASK")));
    }

    @Test
    void delete() {
        repository.delete(1);
        assertEquals(2, repository.size());
    }

    @Test
    void clear() {
        repository.clear();
        assertTrue(repository.isEmpty());
    }
}
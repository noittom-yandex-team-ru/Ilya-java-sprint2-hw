package tasks;

import tasks.enums.StateTask;

import java.util.Objects;

public class Task extends AbstractTask {

    public static class Builder {
        private long id;
        private String name;
        private String description;

        Builder(long id, String name) {
            this.id = id;
            this.name = Objects.requireNonNull(name, "name must not be null");
        }

        Builder id(long id) {
            this.id = id;
            return this;
        }

        Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder description(String description) {
            this.description = description;
            return this;
        }

        Task build() {
            return new Task(this);
        }
    }

    private Task(Builder builder) {
        super(builder.id, builder.name, builder.description);
    }

    public static Task createTask(long id, Task task) {
        return new Builder(id, task.name).description(task.description).build();
    }

    public static Task createTask(String name, String description) {
        return createTask(0, name, description);
    }

    public static Task createTask(String name) {
        return createTask(name, "");
    }

    public static Task createTask(long id, String name) {
        return new Builder(id, name).build();
    }

    public static Task createTask(long id, String name, String description) {
        return new Builder(id, name).description(description).build();
    }

    public Task setTask(Task task) {
        if (task != null) {
            setName(task.name);
            setDescription(task.description);
        }
        return this;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public StateTask getStateTask() {
        return super.getStateTask();
    }

    public void setStateTask(StateTask stateTask) {
        this.stateTask = stateTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description.length='" + (description == null || description.isEmpty() ? 0
                : description.length()) + '\'' +
                ", stateTask=" + stateTask +
                '}';
    }
}

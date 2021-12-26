package tasks;

import java.util.Objects;

public class Task extends AbstractTask {

    public static class Builder {
        private final String id;
        private final String name;
        private String description;

        Builder(String id, String name) {
            this.id = Objects.requireNonNull(id, "id must not be null");
            this.name = Objects.requireNonNull(name, "name must not be null");
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

    public Task createTask(String id, String name) {
        return new Builder(id, name).build();
    }

    public Task createTask(String id, String name, String description) {
        return new Builder(id, name).description(description).build();
    }

    public boolean setTask(Task task) {
        if (id.equals(task.getId())) {
            this.name = task.name;
            this.description = task.description;
            return true;
        }
        return false;
    }

    public String getId() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) && name.equals(task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description.length='" + (description == null || description.isEmpty() ? 0
                : description.length()) + '\'' +
                ", stateTask=" + stateTask +
                '}';
    }
}

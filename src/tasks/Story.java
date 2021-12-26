package tasks;

import tasks.enums.StateTask;

import java.util.Objects;

public final class Story extends AbstractTask {

    private Epic epic;

    public static class Builder {
        private final String id;
        private final String name;
        private final Epic epic;
        private String description;

        Builder(String id, String name, Epic epic) {
            this.id = Objects.requireNonNull(id, "id must not be null");
            this.name = Objects.requireNonNull(name, "name must not be null");
            this.epic = Objects.requireNonNull(epic, "epic must not be null");
        }

        Builder description(String description) {
            this.description = description;
            return this;
        }

        Story build() {
            return new Story(this);
        }
    }


    private Story(Builder builder) {
        super(builder.id, builder.name, builder.description);
        this.epic = builder.epic;
    }

    public static Story createStory(String id, String name, Epic epic) {
        return new Builder(id, name, epic).build();
    }

    public static Story createStory(String id, String name, String description, Epic epic) {
        return new Builder(id, name, epic).description(description).build();
    }

    public boolean setStory(Story story) {
        if (id.equals(story.getId())) {
            setName(story.name);
            setDescription(story.description);
            setEpic(story.epic);
            return true;
        }
        return false;
    }

    void setStateTask(StateTask stateTask) {
        this.stateTask = stateTask;
    }

    public String getId() {
        return id;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
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
        Story story = (Story) o;
        return id.equals(story.id) && name.equals(story.name) && epic.equals(story.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, epic);
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description.length='" + (description == null || description.isEmpty() ? 0
                : description.length()) + '\'' +
                ", stateTask=" + stateTask +
                ", epic=" + epic +
                '}';
    }
}

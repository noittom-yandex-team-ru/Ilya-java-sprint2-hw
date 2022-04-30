package models.tasks;

import models.enums.StateTask;
import models.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Story extends AbstractTask {
    private Epic epic;

    public static class Builder {
        private long id;
        private final String name;
        private final Epic epic;
        private String description;
        private StateTask stateTask;
        private Duration duration;
        private LocalDateTime startTime;

        Builder(String name, Epic epic) {
            this.name = Objects.requireNonNull(name, "name must not be null");
            this.epic = Objects.requireNonNull(epic, "epic must not be null");
        }

        Builder(long id, String name, Epic epic) {
            this.id = id;
            this.name = Objects.requireNonNull(name, "name must not be null");
            this.epic = Objects.requireNonNull(epic, "epic must not be null");
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder stateTask(StateTask stateTask) {
            this.stateTask = stateTask;
            return this;
        }

        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Story build() {
            return new Story(this);
        }
    }

    public static Builder builder(String name, Epic epic) {
        return new Builder(name, epic);
    }

    private Story(Builder builder) {
        super(builder.id, builder.name, builder.description, TypeTask.STORY, builder.stateTask, builder.duration,
                builder.startTime);;
        this.epic = builder.epic;
    }

    public static Story createStory(String name, Epic epic) {
        return new Builder(name, epic).build();
    }

    public static Story createStory(String name, String description, Epic epic) {
        return createStory(0, name, description, epic);
    }

    public static Story createStory(long id, Story story) {
        return new Builder(id, story.name, story.epic)
                .description(story.description)
                .stateTask(story.stateTask)
                .duration(story.duration)
                .startTime(story.startTime)
                .build();
    }

    public static Story createStory(long id, String name, Epic epic) {
        return new Builder(id, name, epic).build();
    }
    public static Story createStory(long id, String name, String description, Epic epic) {
        return new Builder(id, name, epic).description(description).build();
    }

    public static Story createStory(long id, String name, String description, Epic epic, StateTask stateTask) {
        return new Builder(id, name, epic)
                .description(description)
                .stateTask(stateTask)
                .build();
    }

    public static Story createStory(long id, String name, String description, Epic epic, StateTask stateTask,
                                    Duration duration, LocalDateTime startTime) {
        return new Builder(id, name, epic)
                .description(description)
                .stateTask(stateTask)
                .duration(duration)
                .startTime(startTime)
                .build();
    }


    public Story setStory(Story story) {
        if (story != null) {
            setName(story.name);
            setDescription(story.description);
            setEpic(story.epic);
            setDuration(story.duration);
            setStartTime(story.startTime);
        }
        return this;
    }

    public long getId() {
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

    public StateTask getStateTask() {
        return stateTask;
    }

    public void setStateTask(StateTask stateTask) {
        this.stateTask = stateTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Story story = (Story) o;
        return id == story.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}

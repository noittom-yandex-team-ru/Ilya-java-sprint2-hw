package models.tasks;

import models.enums.StateTask;

import java.util.*;

public final class Epic extends AbstractTask {
    private final Map<Long, Story> stories;

    public static class Builder {
        private long id;
        private String name;
        private String description;

        public Builder(long id, String name) {
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

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Epic build() {
            return new Epic(this);
        }
    }

    private Epic(Builder builder) {
        super(builder.id, builder.name, builder.description);
        this.stories = new LinkedHashMap<>();
    }

    public static Epic createEpic(long id, Epic epic) {
        return createEpic(id, epic.name, epic.description, epic.getStories());
    }

    public static Epic createEpic(String name) {
        return createEpic(name, "");
    }

    public static Epic createEpic(String name, String description) {
        return new Builder(0, name).build();
    }

    public static Epic createEpic(long id, String name) {
        return new Builder(id, name).build();
    }

    public static Epic createEpic(long id, String name, String description) {
        return new Builder(id, name).description(description).build();
    }

    public static Epic createEpic(long id, String name, String description, Collection<Story> stories) {
        Epic epic = new Builder(id, name).description(description).build();
        epic.setStories(stories);
        return epic;
    }

    public Story getStory(long id) {
        return stories.get(id);
    }

    public Story addStory(Story story) {
        if (this.id == story.getEpic().getId()) {
            stories.put(story.getId(), story);
            checkState();
        }
        return story;
    }

    public Story updateStory(long id, Story story) {
        return stories.get(id).setStory(story);
    }

    public Story removeStory(long id) {
        Story story = stories.remove(id);
        if (story != null) checkState();
        return story;
    }

    public void removeAllStories() {
        stories.clear();
        stateTask = StateTask.NEW;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Collection<Story> getStories() {
        return stories.values();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public void setDescription(String description) {
        super.setDescription(description);
    }

    public void setStories(Collection<Story> stories) {
        int counterThisEpic = 0;
        for (Story story : stories) {
            if (this.equals(story.getEpic())) {
                counterThisEpic++;
            }
        }
        if (counterThisEpic == stories.size()) {
            this.stories.clear();
            for (Story story : stories) {
                this.stories.put(story.getId(), story);
            }
            checkState();
        }
    }

    public Epic setEpic(Epic epic) {
        if (epic != null) {
            setName(epic.name);
            setDescription(epic.description);
            setStories(epic.stories.values());
            checkState();
        }
        return this;
    }

    public void setStatusStory(long id, StateTask stateTask) {
        Story story = stories.get(id);
        if (id == story.getId()) {
            story.setStateTask(stateTask);
            checkState();
        }
    }

    private void checkState() {
        int counterNewStatusStories = 0;
        int counterDoneStatusStories = 0;
        for (Story story : stories.values()) {
            if (StateTask.NEW.equals(story.stateTask)) counterNewStatusStories++;
            if (StateTask.DONE.equals(story.stateTask)) counterDoneStatusStories++;
        }
        int storiesListLength = stories.size();
        if (counterNewStatusStories == storiesListLength) {
            stateTask = StateTask.NEW;
        } else if (counterDoneStatusStories == storiesListLength) {
            stateTask = StateTask.DONE;
        } else {
            stateTask = StateTask.IN_PROGRESS;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return id == epic.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description.length='" + (description == null || description.isEmpty() ? 0
                : description.length()) + '\'' +
                ", stateTask=" + stateTask +
                ", stories.size=" + stories.size() +
                '}';
    }
}

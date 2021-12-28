package tasks;

import tasks.enums.StateTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public final class Epic extends AbstractTask {
    private Collection<Story> stories;

    public static class Builder {
        private final String id;
        private final String name;
        private String description;

        public Builder(String id, String name) {
            this.id = Objects.requireNonNull(id, "id must not be null");
            this.name = Objects.requireNonNull(name, "name must not be null");
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
        this.stories = new ArrayList<>();
    }

    public static Epic createEpic(String id, String name) {
        return new Builder(id, name).build();
    }

    public static Epic createEpic(String id, String name, String description) {
        return new Builder(id, name).description(description).build();
    }

    public Story getStory(String id) {
        for (Story story : stories) {
            if (id.equals(story.getId())) {
                return story;
            }
        }
        return null;
    }

    public boolean addStory(Story story) {
        if (this.equals(story.getEpic()) && stories.add(story)) {
            checkState();
            return true;
        }
        return false;
    }

    public boolean deleteStory(String id) {
        for (Story story : stories) {
            if (id.equals(story.getId())) {
                stories.remove(story);
                checkState();
                return true;
            }
        }
        return false;
    }

    public void deleteAllStories() {
        stories.clear();
        checkState();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Collection<Story> getStories() {
        return stories;
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
            this.stories = stories;
            checkState();
        }
    }

    public boolean setEpic(Epic epic) {
        if (epic != null && id.equals(epic.getId())) {
            setName(epic.name);
            setDescription(epic.description);
            setStories(epic.stories);
            checkState();
            return true;
        }
        return false;
    }

    private void checkState() {
        int counterNewStatusStories = 0;
        int counterDoneStatusStories = 0;
        for (Story story : stories) {
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
        return id.equals(epic.id) && name.equals(epic.name) && stories.equals(epic.stories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name) * 31 + stories.hashCode();
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

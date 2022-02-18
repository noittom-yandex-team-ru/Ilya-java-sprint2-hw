package repositories.tasks;

import models.tasks.Epic;
import models.tasks.Story;

import java.util.*;

public class EpicsRepository extends AbstractTasksRepository<Epic> {
    private final Map<Long, Epic> idEpicMap;

    public EpicsRepository() {
        super();
        idEpicMap = new HashMap<>();
    }

    public EpicsRepository(Collection<Epic> epics) {
        this();
        Objects.requireNonNull(epics, "epics must not be null");
        for (Epic epic : epics) {
            idEpicMap.put(++counter, Epic.createEpic(counter, epic));
        }
    }

    @Override
    public Collection<Epic> findAll() {
        return idEpicMap.values();
    }

    @Override
    public Epic find(long id) {
        return idEpicMap.get(id);
    }

    @Override
    public Epic add(Epic epic) {
        return idEpicMap.put(++counter, Epic.createEpic(counter, epic));
    }

    @Override
    public Epic update(long id, Epic newEpic) {
        Epic currentEpic = idEpicMap.get(id);
        if (currentEpic != null) {
            return currentEpic.setEpic(newEpic);
        }
        return null;
    }

    @Override
    public Epic delete(long id) {
        return idEpicMap.remove(id);
    }

    @Override
    public void clear() {
        idEpicMap.clear();
    }

    @Override
    public int size() {
        return idEpicMap.size();
    }

    public Collection<Story> findAllStories(Epic epic) {
        Epic mapEpic = idEpicMap.get(epic.getId());
        if (mapEpic == null) return Collections.emptyList();
        return epic.getStories();
    }

    public Story findStory(long id, Epic epic) {
        Epic mapEpic = idEpicMap.get(epic.getId());
        if (mapEpic == null) return null;
        return mapEpic.getStory(id);
    }

    public Story findStory(long id) {
        Story story;
        for (Epic epic : idEpicMap.values()) {
            if ((story = findStory(id, epic)) != null) return story;
        }
        return null;
    }

    public Story addStory(Story story, Epic epic) {
        Epic mapEpic = idEpicMap.get(epic.getId());
        if (mapEpic == null) return null;
        return mapEpic.addStory(Story.createStory(++counter, story));
    }

    public Story updateStory(long id, Story story) {
        Story mapStory = findStory(id);
        if (mapStory != null) {
            Epic currentEpic = mapStory.getEpic();
            if (currentEpic.getId() == story.getEpic().getId()) {
                return currentEpic.updateStory(id, story);
            }
            deleteStory(id, currentEpic);
            return addStory(story, story.getEpic());
        }
        return null;
    }

    public Story deleteStory(long id) {
        Story story = findStory(id);
        if (story == null) return null;
        return deleteStory(id, story.getEpic());
    }

    public Story deleteStory(long id, Epic epic) {
        Epic mapEpic = idEpicMap.get(epic.getId());
        if (mapEpic == null) return null;
        return mapEpic.removeStory(id);
    }

    public void clearStories(Epic epic) {
        idEpicMap.get(epic.getId()).removeAllStories();
    }
}

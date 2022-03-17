package repositories.tasks;

import models.tasks.Epic;
import models.tasks.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicsRepositoryTest {

    private static EpicsRepository repository;

    private static Epic REPOSITORY_EPIC_1;
    private static Epic REPOSITORY_EPIC_2;
    private static Epic REPOSITORY_EPIC_3;

    private final static List<Epic> epics = List.of(
            Epic.createEpic("Epic1"),
            Epic.createEpic("Epic2"),
            Epic.createEpic("Epic3")
    );

    @BeforeEach
    void initEpicsRepository() {
        repository = new EpicsRepository(epics);

        REPOSITORY_EPIC_1 = repository.find(1);
        REPOSITORY_EPIC_2 = repository.find(2);
        REPOSITORY_EPIC_3 = repository.find(3);

        repository.addStory(Story.createStory("Story1", "StoryOwnedEpic1", REPOSITORY_EPIC_1),
                REPOSITORY_EPIC_1);
        repository.addStory(Story.createStory("Story2", "StoryOwnedEpic1", REPOSITORY_EPIC_1),
                REPOSITORY_EPIC_1);
        repository.addStory(Story.createStory("Story3", "StoryOwnedEpic1", REPOSITORY_EPIC_1),
                REPOSITORY_EPIC_1);
        repository.addStory(Story.createStory("Story4", "StoryOwnedEpic2", REPOSITORY_EPIC_2),
                REPOSITORY_EPIC_2);
    }

    @Test
    void findAll() {
        Epic[] actualEpics = repository.findAll().toArray(Epic[]::new);
        assertEquals(3, actualEpics.length);
        for (int i = 1; i < 4; i++) {
            Epic epic = actualEpics[i - 1];
            assertEquals(i, epic.getId());
            assertEquals("Epic" + i, epic.getName());
        }
        assertEquals(3, actualEpics[0].getStories().size());
        assertEquals(1, actualEpics[1].getStories().size());
        assertTrue(actualEpics[2].getStories().isEmpty());
    }

    @Test
    void find() {
        Epic epic = repository.find(2);
        assertEquals(2, epic.getId());
        assertEquals("Epic2", epic.getName());
        assertEquals(1, epic.getStories().size());
    }

    @Test
    void shouldReturnNullWhenFindEpicByNonexistentId() {
        assertNull(repository.find(100));
    }

    @Test
    void add() {
        final Epic epic = Epic.createEpic("Epic");
        repository.add(epic);
        assertEquals(8, repository.find(8).getId());
        assertEquals(4, repository.size());
    }

    @Test
    void update() {
        final Epic epic = Epic.createEpic(10, "EPIC", "IT'S AN EPIC");
        repository.update(3, epic);
        Epic actualEpic = repository.find(3);
        assertEquals(3, actualEpic.getId());
        assertEquals("EPIC", actualEpic.getName());
        assertEquals("IT'S AN EPIC", actualEpic.getDescription());
    }

    @Test
    void shouldReturnNullWhenUpdateEpicByNonexistentId() {
        assertNull(repository.update(100, Epic.createEpic(10, "EPIC", "IT'S AN EPIC")));
    }


    @Test
    void delete() {
        repository.delete(2);
        assertEquals(2, repository.size());
    }

    @Test
    void clear() {
        repository.clear();
        assertTrue(repository.isEmpty());
    }

    @Test
    void findAllStories() {
        final Story[] actualStoriesOwnedEpic1 = repository.findAllStories(REPOSITORY_EPIC_1).toArray(Story[]::new);
        final Story[] actualStoriesOwnedEpic2 = repository.findAllStories(REPOSITORY_EPIC_2).toArray(Story[]::new);
        final Story[] actualStoriesOwnedEpic3 = repository.findAllStories(REPOSITORY_EPIC_3).toArray(Story[]::new);
        assertEquals(3, actualStoriesOwnedEpic1.length);
        assertEquals(1, actualStoriesOwnedEpic2.length);
        assertEquals(0, actualStoriesOwnedEpic3.length);
        assertEqualsOwnedEpic(actualStoriesOwnedEpic1, REPOSITORY_EPIC_1);
        assertEqualsOwnedEpic(actualStoriesOwnedEpic2, REPOSITORY_EPIC_2);
        assertEqualsOwnedEpic(actualStoriesOwnedEpic3, REPOSITORY_EPIC_3);
    }

    private void assertEqualsOwnedEpic(Story[] stories, Epic epic) {
        for (Story story : stories) {
            assertEquals(epic, story.getEpic());
        }
    }

    @Test
    void findStory() {
        final Story story = repository.findStory(5);
        assertEquals(5, story.getId());
        assertEquals("Story2", story.getName());
        assertEquals("StoryOwnedEpic1", story.getDescription());
        assertEquals(REPOSITORY_EPIC_1, story.getEpic());
    }

    @Test
    void shouldReturnNullWhenFindStoryByNonexistentId() {
        assertNull(repository.findStory(100));
    }

    @Test
    void addStory() {
        final Story story5 = Story.createStory("Story5", "StoryOwnedEpic3", REPOSITORY_EPIC_3);
        repository.addStory(story5, story5.getEpic());
        assertEquals(1, REPOSITORY_EPIC_3.getStories().size());
    }

    @Test
    void updateStory() {
        repository.updateStory(7, Story.createStory("STORY", "IT'S A STORY", REPOSITORY_EPIC_3));
        assertEquals(0, REPOSITORY_EPIC_2.getStories().size());
        assertEquals(1, REPOSITORY_EPIC_3.getStories().size());
        final Story story = repository.findStory(7);
        assertEquals("STORY", story.getName());
        assertEquals("IT'S A STORY", story.getDescription());
    }

    @Test
    void shouldReturnNullWhenUpdateStoryByNonexistentId() {
        assertNull(repository.updateStory(100, Story.createStory("STORY", "IT'S A STORY", REPOSITORY_EPIC_3)));
    }

    @Test
    void deleteStory() {
        repository.deleteStory(7);
        assertEquals(0, REPOSITORY_EPIC_2.getStories().size());
    }

    @Test
    void clearStories() {
        repository.clearStories(REPOSITORY_EPIC_1);
        assertEquals(0, REPOSITORY_EPIC_1.getStories().size());
    }
}
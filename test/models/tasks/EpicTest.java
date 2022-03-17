package models.tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EpicTest {
    private static Epic TEST_EPIC;
    private static Story TEST_STORY;

    @BeforeAll
    public static void init() {
        TEST_EPIC = Epic.createEpic("TestEpic");
        TEST_STORY = Story.createStory("TestStory", TEST_EPIC);
    }

    @Test
    @BeforeEach
    public void addStoryTest() {
        assertEquals(TEST_STORY, TEST_EPIC.addStory(TEST_STORY));
        assertEquals(1, TEST_EPIC.getStories().size());
    }

    @Test
    public void getStoryTest() {
        assertEquals(TEST_STORY, TEST_EPIC.getStory(TEST_STORY.id));
    }

    @Test
    public void updateStoryTest() {
        assertEquals(TEST_STORY, TEST_EPIC.updateStory(TEST_STORY.id, TEST_STORY));
    }

    @Test
    public void removeStoryTest() {
        assertEquals(TEST_STORY, TEST_EPIC.removeStory(TEST_STORY.id));
        assertEquals(0, TEST_EPIC.getStories().size());
    }

    @Test
    public void removeAllStoriesTest() {
        TEST_EPIC.removeAllStories();
        assertEquals(0, TEST_EPIC.getStories().size());
    }
}

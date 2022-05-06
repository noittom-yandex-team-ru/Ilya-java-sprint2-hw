package web.taskSerializers.jsonAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import models.tasks.Story;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class StoriesAdapter extends TypeAdapter<Collection<Story>> {
    @Override
    public void write(JsonWriter jsonWriter, Collection<Story> stories) throws IOException {
        jsonWriter.beginArray();
        for (Story story : stories) {
            jsonWriter.value(story.getId());
        }
        jsonWriter.endArray();
    }

    @Override
    public Collection<Story> read(final JsonReader jsonReader) throws IOException {
        return Collections.emptyList();
    }
}

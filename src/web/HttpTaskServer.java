package web;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import managers.FileBackedAppManager;
import models.tasks.Epic;
import models.tasks.Story;
import models.tasks.Task;
import utils.Managers;
import web.handlers.EpicsHandler;
import web.handlers.StoriesHandler;
import web.handlers.TasksHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;


public class HttpTaskServer {
    private static final int PORT = 8080;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final Gson GSON = new Gson();
    private static final FileBackedAppManager TASK_MANAGER = Managers.getFileBacked(Path.of("temp.csv"));

    public static void main(String[] args) throws IOException {
        TASK_MANAGER.addTask(Task.createTask("Task1"));
        TASK_MANAGER.addTask(Task.createTask("Task2"));
        TASK_MANAGER.addEpic(Epic.createEpic("Epic1"));
        TASK_MANAGER.addEpic(Epic.createEpic("Epic2"));
        TASK_MANAGER.addStory(Story.createStory("Story1", TASK_MANAGER.findEpic(3)));
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TasksHandler(TASK_MANAGER));
        //httpServer.createContext("/tasks/task/", new TaskHandler(TASK_MANAGER));
        httpServer.createContext("/tasks/epic", new EpicsHandler(TASK_MANAGER));
        httpServer.createContext("/tasks/stories/epic/", new StoriesHandler(TASK_MANAGER));
        httpServer.start();
    }

}

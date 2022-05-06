package web.handlers;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.AppManager;
import web.HttpTaskServer;
import web.taskSerializers.jsonAdapters.StoriesAdapter;

import java.io.IOException;
import java.util.Collection;

public class EpicsHandler implements HttpHandler {
    private final AppManager manager;

    public EpicsHandler(AppManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Collection.class, new StoriesAdapter());
                    String response = gsonBuilder.create().toJson(manager.findAllEpics());
                    exchange.getResponseHeaders().add("content-type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes(HttpTaskServer.DEFAULT_CHARSET));
                    System.out.println("The epics were received successfully");
                    break;
                case "DELETED":
                    manager.deleteAllTasks();
                    exchange.sendResponseHeaders(204, -1);
                    System.out.println("The epics were removed successfully");
                default:
                    exchange.sendResponseHeaders(400, -1);
                    System.out.println("This context can only work with the following methods: GET, DELETE");
            }
        }
        finally {
            exchange.sendResponseHeaders(500, -1);
        }
    }
}

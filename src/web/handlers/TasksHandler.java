package web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.AppManager;
import web.HttpTaskServer;

import java.io.IOException;

public class TasksHandler implements HttpHandler {
    private final AppManager manager;

    public TasksHandler(AppManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    String response = HttpTaskServer.GSON.toJson(manager.findAllTasks());
                    exchange.getResponseHeaders().add("content-type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes(HttpTaskServer.DEFAULT_CHARSET));
                    System.out.println("The tasks were received successfully");
                    break;
                case "DELETED":
                    manager.deleteAllTasks();
                    exchange.sendResponseHeaders(204, -1);
                    System.out.println("The tasks were removed successfully");
                default:
                    exchange.sendResponseHeaders(400, -1);
                    System.out.println("This context can only work with the following methods: GET, DELETE");
            }
        } finally {
            exchange.sendResponseHeaders(500, -1);
        }
    }
}

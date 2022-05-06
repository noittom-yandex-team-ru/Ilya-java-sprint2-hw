package web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.AppManager;
import models.tasks.Story;
import utils.Web;
import web.HttpTaskServer;

import java.io.IOException;
import java.util.Map;

public class StoriesHandler implements HttpHandler {
    private final int ID_QUERY_INDEX = 0;
    private final AppManager manager;

    public StoriesHandler(AppManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    Map.Entry<String, String> id = Web.getQueryParameters(exchange.getRequestURI()).get(ID_QUERY_INDEX);
                    if ("id".equals(id.getKey())) {
                        String response = HttpTaskServer.GSON.toJson(manager.findAllStories(Long.parseLong(id.getValue())));
                        exchange.getResponseHeaders().add("content-type", "application/json");
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes(HttpTaskServer.DEFAULT_CHARSET));
                        System.out.println("The tasks were received successfully");
                    }
                    break;
                case "DELETED":
                    manager.deleteAllTasks();
                    exchange.sendResponseHeaders(204, -1);
                    System.out.println("The tasks were removed successfully");
                default:
                    exchange.sendResponseHeaders(400, -1);
                    System.out.println("This context can only work with the following methods: GET, DELETE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            exchange.sendResponseHeaders(500, -1);
        }
    }
}

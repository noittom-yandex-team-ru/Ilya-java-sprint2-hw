package tasks;

import tasks.enums.StateTask;

abstract class AbstractTask {
    protected final String id;
    protected String name;
    protected String description;
    protected StateTask stateTask = StateTask.NEW;

    protected AbstractTask(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    protected String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public StateTask getStateTask() {
        return stateTask;
    }
}

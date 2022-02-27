package models.tasks;

import models.enums.StateTask;
import models.enums.TypeTask;

public abstract class AbstractTask {
    protected final long id;
    protected String name;
    protected String description;
    protected TypeTask typeTask;
    protected StateTask stateTask;

    protected AbstractTask(Long id, String name, String description, TypeTask typeTask, StateTask stateTask) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.typeTask = typeTask;
        this.stateTask = stateTask == null ? StateTask.NEW : stateTask;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public StateTask getStateTask() {
        return stateTask;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }
}

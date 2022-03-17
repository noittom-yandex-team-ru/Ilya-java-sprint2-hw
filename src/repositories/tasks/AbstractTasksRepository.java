package repositories.tasks;

import models.tasks.AbstractTask;
import utils.TaskCounter;

abstract class AbstractTasksRepository<T extends AbstractTask> implements ITasksRepository<T> {
    protected TaskCounter counter;

    protected AbstractTasksRepository() {
        counter = new TaskCounter();
    }

    public abstract int size();
}

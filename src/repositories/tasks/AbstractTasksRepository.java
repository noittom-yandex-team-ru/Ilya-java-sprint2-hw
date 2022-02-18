package repositories.tasks;

import models.tasks.AbstractTask;

abstract class AbstractTasksRepository<T extends AbstractTask> implements ITasksRepository<T> {
    protected static long counter = 0;

    protected AbstractTasksRepository() {

    }

    public abstract int size();
}

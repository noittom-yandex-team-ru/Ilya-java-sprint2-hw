package repositories.tasks;

import tasks.AbstractTask;

import java.util.Collection;

interface ITasksRepository<T extends AbstractTask> {
    Collection<T> findAll();

    T find(long id);

    T add(T abstractTask);

    T update(long id, T abstractTask);

    T delete(long id);

    void clear();
}

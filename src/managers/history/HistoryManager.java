package managers.history;

import tasks.AbstractTask;

import java.util.List;

public interface HistoryManager {
    void add(AbstractTask task);

    void remove(long id);

    List<AbstractTask> getHistory();
}

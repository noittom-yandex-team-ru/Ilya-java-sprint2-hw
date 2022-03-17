package utils;

public class TaskCounter {
    private long counter;

    public TaskCounter(long startValue) {
        counter = startValue;
    }

    public TaskCounter() {
        counter = 0;
    }

    public long getValue() {
        return counter;
    }

    public long increment() {
        return ++counter;
    }

    public long reset() {
        return counter = 0;
    }
}

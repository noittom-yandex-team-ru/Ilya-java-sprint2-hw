package exceptions;

import models.enums.TypeTask;

public class ManagerWrongIdException extends RuntimeException {
    private ManagerWrongIdException(final String message) {
        super(message);
    }

    public ManagerWrongIdException(final TypeTask typeTask, final long id) {
        this(typeTask + " c таким id: " + id + " не существует.");
    }
}

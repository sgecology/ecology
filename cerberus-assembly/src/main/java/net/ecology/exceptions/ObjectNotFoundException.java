package net.ecology.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3891534644498426670L;

    public ObjectNotFoundException(String objectId) {
        super("No such object with id: " + objectId);
    }

    public ObjectNotFoundException(Throwable throwable) {
      super(throwable);
  }
}


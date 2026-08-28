package mx.egd.fmre.register.exception;

import mx.egd.fmre.register.service.exceptions.ServiceException;

public class StorageException extends ServiceException {

  private static final long serialVersionUID = 3476786544073378642L;

  public StorageException(String message) {
    super(message);
  }

  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
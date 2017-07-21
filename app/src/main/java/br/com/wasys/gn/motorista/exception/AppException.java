package br.com.wasys.gn.motorista.exception;

/**
 * Created by pascke on 30/08/16.
 */
public class AppException extends Throwable {

    public AppException(String detailMessage) {
        super(detailMessage);
    }

    public AppException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }
}

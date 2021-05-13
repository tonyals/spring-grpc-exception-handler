package br.com.tony.grpc.exceptions;

import io.grpc.Status;

public final class AlreadyExistsException extends BusinessException {

    public AlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public Status getStatus() {
        return Status.ALREADY_EXISTS;
    }
}

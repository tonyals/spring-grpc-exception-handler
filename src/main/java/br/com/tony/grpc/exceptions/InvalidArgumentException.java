package br.com.tony.grpc.exceptions;

import io.grpc.Status;

public final class InvalidArgumentException extends BusinessException {

    private static final Status status = Status.INVALID_ARGUMENT;

    public InvalidArgumentException(String message) {
        super(message);
    }

    @Override
    public Status getStatus() {
        return Status.INVALID_ARGUMENT;
    }
}

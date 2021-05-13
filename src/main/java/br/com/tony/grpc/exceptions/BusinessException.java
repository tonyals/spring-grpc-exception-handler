package br.com.tony.grpc.exceptions;

import io.grpc.Status;

public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public abstract Status getStatus();
}

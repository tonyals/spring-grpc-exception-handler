package br.com.tony.grpc.resources.handler;

import br.com.tony.grpc.exceptions.BusinessException;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import static br.com.tony.grpc.constants.AppConstants.INTERNAL_SERVER_ERROR;

@GrpcAdvice
public class ExceptionHandler {

    @GrpcExceptionHandler(BusinessException.class)
    public StatusRuntimeException handleBusinessException(BusinessException e) {
        return e.getStatus()
                .withCause(e.getCause()).withDescription(e.getMessage()).asRuntimeException();
    }

    @GrpcExceptionHandler(Exception.class)
    public StatusException handleException(Exception e) {
        return Status.INTERNAL.withCause(e).withDescription(INTERNAL_SERVER_ERROR).asException();
    }
}

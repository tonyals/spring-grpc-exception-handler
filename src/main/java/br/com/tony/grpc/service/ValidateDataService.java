package br.com.tony.grpc.service;

import br.com.tony.grpc.CreatePersonRequest;

public interface ValidateDataService {
    void validatePersonRequest(CreatePersonRequest request);
}

package br.com.tony.grpc.service.impl;

import br.com.tony.grpc.CreatePersonRequest;
import br.com.tony.grpc.constants.AppConstants;
import br.com.tony.grpc.exceptions.InvalidArgumentException;
import br.com.tony.grpc.service.ValidateDataService;
import org.springframework.stereotype.Service;

@Service
public class ValidateDataServiceImpl implements ValidateDataService {

    @Override
    public void validatePersonRequest(CreatePersonRequest request) {
        if (!request.hasEmail() || request.getEmail().getValue().isBlank())
            throw new InvalidArgumentException(AppConstants.EMAIL_IS_REQUIRED);

        if (!request.hasName() || request.getName().getValue().isBlank())
            throw new InvalidArgumentException(AppConstants.NAME_IS_REQUIRED);
    }
}

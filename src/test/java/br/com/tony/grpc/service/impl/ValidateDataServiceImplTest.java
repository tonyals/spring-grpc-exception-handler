package br.com.tony.grpc.service.impl;

import br.com.tony.grpc.CreatePersonRequest;
import br.com.tony.grpc.constants.AppConstants;
import br.com.tony.grpc.exceptions.InvalidArgumentException;
import br.com.tony.grpc.service.ValidateDataService;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidateDataServiceImplTest {

    private final ValidateDataService validateDataService = new ValidateDataServiceImpl();

    @Test
    @DisplayName("should not return InvalidArgumentException if name is email is valid")
    void validateSuccessTest() {
        var validRequest = CreatePersonRequest.newBuilder()
                .setName(StringValue.of("any name"))
                .setEmail(StringValue.of("any@mail.com"))
                .build();

        assertDoesNotThrow(() -> validateDataService.validatePersonRequest(validRequest));
    }

    @Test
    @DisplayName("should return InvalidArgumentException if email is empty")
    void validateEmailEmptyThrowsInvalidArgumentExceptionTest() {
        var invalidEmailRequest = CreatePersonRequest.newBuilder()
                .setName(StringValue.of("Any name"))
                .setEmail(StringValue.of(""))
                .build();

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> validateDataService.validatePersonRequest(invalidEmailRequest));

        assertEquals(exception.getMessage(), AppConstants.EMAIL_IS_REQUIRED);
    }

    @Test
    @DisplayName("should return InvalidArgumentException if name is empty")
    void validateNameEmptyThrowsInvalidArgumentExceptionTest() {
        var invalidNameRequest = CreatePersonRequest.newBuilder()
                .setName(StringValue.of(""))
                .setEmail(StringValue.of("any@mail.com"))
                .build();

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> validateDataService.validatePersonRequest(invalidNameRequest));

        assertEquals(exception.getMessage(), AppConstants.NAME_IS_REQUIRED);
    }
}

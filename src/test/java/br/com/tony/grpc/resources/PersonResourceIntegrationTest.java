package br.com.tony.grpc.resources;

import br.com.tony.grpc.CreatePersonRequest;
import br.com.tony.grpc.CreatePersonResponse;
import br.com.tony.grpc.CreatePersonServiceGrpc;
import br.com.tony.grpc.IntegrationTestConfiguration;
import br.com.tony.grpc.constants.AppConstants;
import br.com.tony.grpc.dto.PersonOutputDTO;
import br.com.tony.grpc.exceptions.AlreadyExistsException;
import br.com.tony.grpc.exceptions.InvalidArgumentException;
import br.com.tony.grpc.service.PersonService;
import br.com.tony.grpc.service.ValidateDataService;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static br.com.tony.grpc.constants.AppConstants.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "grpc.server.inProcessName=test", // Enable inProcess server
        "grpc.server.port=-1", // Disable external server
        "grpc.client.inProcess.address=in-process:test" // Configure the client to connect to the inProcess server
})
@SpringJUnitConfig(classes = {IntegrationTestConfiguration.class})
// Spring doesn't start without a config (might be empty)
@DirtiesContext // Ensures that the grpc-server is properly shutdown after each test
// Avoids "port already in use" during tests
@AutoConfigureMockMvc
public class PersonResourceIntegrationTest {

    @GrpcClient("inProcess")
    private CreatePersonServiceGrpc.CreatePersonServiceBlockingStub createPersonService;

    private static final Long ID = 1L;
    private static final String NAME = "any_name";
    private static final String EMAIL = "any_mail@mail.com";

    @MockBean
    private PersonService personService;

    @MockBean
    private ValidateDataService validateDataService;

    @Test
    @DirtiesContext
    @DisplayName("Should return a CreatePersonResponse if create person success")
    public void testCreatePersonSuccess() {

        when(personService.create(any())).thenReturn(personOutputDTOStub());

        CreatePersonResponse response = createPersonService.create(createPersonRequestStub());

        assertEquals(Int64Value.of(ID), response.getId());
        assertEquals(StringValue.of(NAME), response.getName());
        assertEquals(StringValue.of(EMAIL), response.getEmail());
    }

    @Test
    @DirtiesContext
    @DisplayName("Should return a InvalidArgumentException if email is empty")
    public void testCreatePersonThrowsInvalidArgumentExceptionIfEmailIsEmpty() {

        var invalidEmailRequest = CreatePersonRequest.newBuilder()
                .setName(StringValue.of("Any name"))
                .setEmail(StringValue.of(""))
                .build();

        doThrow(new InvalidArgumentException(AppConstants.EMAIL_IS_REQUIRED))
                .when(validateDataService).validatePersonRequest(invalidEmailRequest);

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class,
                () -> createPersonService.create(invalidEmailRequest));

        assertEquals(exception.getStatus().getCode(), Status.INVALID_ARGUMENT.getCode());
        assertEquals(exception.getStatus().getDescription(), AppConstants.EMAIL_IS_REQUIRED);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should return a InvalidArgumentException if name is empty")
    public void testCreatePersonThrowsInvalidArgumentExceptionIfNameIsEmpty() {

        var invalidNameRequest = CreatePersonRequest.newBuilder()
                .setName(StringValue.of(""))
                .setEmail(StringValue.of("any@mail.com"))
                .build();

        doThrow(new InvalidArgumentException(AppConstants.NAME_IS_REQUIRED))
                .when(validateDataService).validatePersonRequest(invalidNameRequest);

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> createPersonService.create(invalidNameRequest));

        assertEquals(exception.getStatus().getCode(), Status.INVALID_ARGUMENT.getCode());
        assertEquals(exception.getStatus().getDescription(), AppConstants.NAME_IS_REQUIRED);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should return a AlreadyExistsException if the name is already registered")
    public void testCreatePersonThrowsAlreadyExistsExceptionIfNameAlreadyRegistered() {

        doThrow(new AlreadyExistsException(AppConstants.NAME_ALREADY_EXISTS))
                .when(validateDataService).validatePersonRequest(createPersonRequestStub());

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> createPersonService.create(createPersonRequestStub()));

        assertEquals(exception.getStatus().getCode(), Status.ALREADY_EXISTS.getCode());
        assertEquals(exception.getStatus().getDescription(), AppConstants.NAME_ALREADY_EXISTS);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should return a InternalError if the server is error")
    public void testCreatePersonThrowsInternalError() {

        when(personService.create(any()))
                .thenAnswer(invocation -> {
                            throw new StatusException(Status.INTERNAL.withDescription(INTERNAL_SERVER_ERROR));
                        }
                );

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> createPersonService.create(createPersonRequestStub()));

        assertEquals(exception.getStatus().getCode(), Status.INTERNAL.getCode());
        assertEquals(exception.getStatus().getDescription(), INTERNAL_SERVER_ERROR);
    }

    private PersonOutputDTO personOutputDTOStub() {
        return PersonOutputDTO.builder()
                .setId(ID)
                .setName(NAME)
                .setEmail(EMAIL).build();
    }

    private CreatePersonRequest createPersonRequestStub() {
        return CreatePersonRequest
                .newBuilder()
                .setName(StringValue.newBuilder().setValue(NAME).build())
                .setEmail(StringValue.newBuilder().setValue(EMAIL).build())
                .build();
    }
}

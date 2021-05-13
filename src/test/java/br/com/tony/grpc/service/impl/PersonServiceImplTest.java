package br.com.tony.grpc.service.impl;

import br.com.tony.grpc.constants.AppConstants;
import br.com.tony.grpc.dto.PersonInputDTO;
import br.com.tony.grpc.dto.PersonOutputDTO;
import br.com.tony.grpc.entity.Person;
import br.com.tony.grpc.exceptions.AlreadyExistsException;
import br.com.tony.grpc.repository.PersonRepository;
import br.com.tony.grpc.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {

    private final PersonRepository personRepository = mock(PersonRepository.class);
    private final PersonService personService = new PersonServiceImpl(personRepository);

    @Test
    @DisplayName("should create person returns a PersonOutputDTO in success")
    void createPersonSuccessTest() {

        when(personRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        when(personRepository.save(any()))
                .thenReturn(personStub());

        PersonOutputDTO outputDTO = personService.create(
                PersonInputDTO.builder()
                        .setName("any name")
                        .setEmail("any@mail.com").build());

        assertNotNull(outputDTO);
        assertEquals(outputDTO.getName(), personStub().getName());
        assertEquals(outputDTO.getEmail(), personStub().getEmail());
    }

    @Test
    @DisplayName("should create person returns a AlreadyExistsException if name is duplicated")
    void createPersonThrowsAlreadyExistsExceptionTest() {

        when(personRepository.findByName(anyString()))
                .thenReturn(Optional.of(personStub()));

        when(personRepository.save(any()))
                .thenReturn(personStub());

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> personService.create(
                PersonInputDTO.builder()
                        .setName("any name")
                        .setEmail("any@mail.com").build()));

        assertEquals(exception.getMessage(), AppConstants.NAME_ALREADY_EXISTS);
    }

    private Person personStub() {
        return new Person("any name", "any@mail.com");
    }

}

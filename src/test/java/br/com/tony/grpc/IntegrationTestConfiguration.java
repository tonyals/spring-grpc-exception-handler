package br.com.tony.grpc;

import br.com.tony.grpc.repository.PersonRepository;
import br.com.tony.grpc.resources.PersonResource;
import br.com.tony.grpc.resources.handler.ExceptionHandler;
import br.com.tony.grpc.service.PersonService;
import br.com.tony.grpc.service.ValidateDataService;
import br.com.tony.grpc.service.impl.PersonServiceImpl;
import br.com.tony.grpc.service.impl.ValidateDataServiceImpl;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcAdviceAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
@ImportAutoConfiguration({
        GrpcServerAutoConfiguration.class, // Create required server beans
        GrpcServerFactoryAutoConfiguration.class, // Select server implementation
        GrpcClientAutoConfiguration.class,
        GrpcAdviceAutoConfiguration.class}) // Support @GrpcClient annotation
public class IntegrationTestConfiguration {

    @Bean
    PersonResource personResource() {
        return new PersonResource(personService(), validateDataService());
    }

    @Bean
    PersonService personService() {
        return new PersonServiceImpl(mock(PersonRepository.class));
    }

    @Bean
    ValidateDataService validateDataService() {
        return new ValidateDataServiceImpl();
    }

    @Bean
    ExceptionHandler exceptionHandler() {
        return new ExceptionHandler();
    }
}

package br.com.tony.grpc.resources;

import br.com.tony.grpc.CreatePersonRequest;
import br.com.tony.grpc.CreatePersonResponse;
import br.com.tony.grpc.CreatePersonServiceGrpc;
import br.com.tony.grpc.dto.PersonInputDTO;
import br.com.tony.grpc.dto.PersonOutputDTO;
import br.com.tony.grpc.service.PersonService;
import br.com.tony.grpc.service.ValidateDataService;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class PersonResource
        extends CreatePersonServiceGrpc.CreatePersonServiceImplBase {

    private final PersonService personService;
    private final ValidateDataService validateDataService;

    public PersonResource(PersonService personService, ValidateDataService validateDataService) {
        this.personService = personService;
        this.validateDataService = validateDataService;
    }

    @Override
    public void create(CreatePersonRequest request, StreamObserver<CreatePersonResponse> responseObserver) {
            validateDataService.validatePersonRequest(request);

            PersonOutputDTO outputDTO = personService.create(PersonInputDTO.builder()
                    .setName(request.getName().getValue())
                    .setEmail(request.getEmail().getValue()))
                    .build();

            responseObserver.onNext(CreatePersonResponse
                    .newBuilder()
                    .setId(Int64Value.of(outputDTO.getId()))
                    .setName(StringValue.newBuilder().setValue(outputDTO.getName()).build())
                    .setEmail(StringValue.newBuilder().setValue(outputDTO.getEmail()).build())
                    .build());

            responseObserver.onCompleted();
    }
}

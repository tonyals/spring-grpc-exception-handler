package br.com.tony.grpc.service;

import br.com.tony.grpc.dto.PersonInputDTO;
import br.com.tony.grpc.dto.PersonOutputDTO;

public interface PersonService {
    PersonOutputDTO create(PersonInputDTO person);
}

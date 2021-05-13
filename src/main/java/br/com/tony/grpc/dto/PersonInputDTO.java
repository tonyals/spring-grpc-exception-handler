package br.com.tony.grpc.dto;

public class PersonInputDTO {

    private String name;
    private String email;

    private PersonInputDTO() {
    }

    private PersonInputDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static PersonInputDTO builder() {
        return new PersonInputDTO();
    }

    public PersonInputDTO setName(String name) {
        this.name = name;
        return this;
    }

    public PersonInputDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public PersonInputDTO build() {
        return new PersonInputDTO(this.name, this.email);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

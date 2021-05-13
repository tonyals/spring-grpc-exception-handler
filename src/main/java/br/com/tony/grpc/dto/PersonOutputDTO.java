package br.com.tony.grpc.dto;

public class PersonOutputDTO {
    private Long id;
    private String name;
    private String email;

    private PersonOutputDTO(){}

    private PersonOutputDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public static PersonOutputDTO builder() {
        return new PersonOutputDTO();
    }

    public PersonOutputDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PersonOutputDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PersonOutputDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public PersonOutputDTO build() {
        return new PersonOutputDTO(this.id, this.name, this.email);
    }
}

package com.github.ukase.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonDto {
    private String familyName;
    private String firstName;
    private String patronymic;
    private String email;
    private String phone;
}

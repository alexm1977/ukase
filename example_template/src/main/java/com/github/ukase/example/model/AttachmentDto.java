package com.github.ukase.example.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentDto {
    private String fileName;
    private String description;
}

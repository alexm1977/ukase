package com.github.ukase.example.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReportRequestDto {
    private String requestNumber;
    private PersonDto person;
    private String message;
    private List<AttachmentDto> attachment;
}

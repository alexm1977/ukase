package com.github.ukase.example.services;

import com.github.ukase.client.UkaseService;
import com.github.ukase.example.config.ExampleProperties;
import com.github.ukase.example.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class ReportService {

    private static final String TEMPLATE_NAME = "upload://example";

    private final UkaseService ukaseService;
    private final ExampleProperties exampleProperties;

    public ReportService(UkaseService ukaseService,
                         ExampleProperties exampleProperties) {
        this.ukaseService = ukaseService;
        this.exampleProperties = exampleProperties;
    }

    public File createPdf() {

        ReportRequestDto reportRequestDto = ReportRequestDto
                .builder()
                .requestNumber("123456ABC")
                .person(PersonDto
                        .builder()
                        .familyName("Иванов")
                        .firstName("Иван")
                        .patronymic("Иванович")
                        .email("my_email@email.com")
                        .phone("0987654321")
                        .build())
                .message("Таким образом, выбранный нами инновационный путь играет важную роль в формировании форм воздействия. Не следует, однако, забывать о том, что консультация с профессионалами из IT играет важную роль в формировании форм воздействия. С другой стороны постоянное информационно-техническое обеспечение нашей деятельности обеспечивает широкому кругу специалистов участие в формировании дальнейших направлений развития проекта. Разнообразный и богатый опыт консультация с профессионалами из IT влечет за собой процесс внедрения и модернизации системы масштабного изменения ряда параметров? Задача организации, в особенности же начало повседневной работы по формированию позиции представляет собой интересный эксперимент проверки новых предложений. Не следует, однако, забывать о том, что постоянный количественный рост...")
                .attachment(List.of(
                        AttachmentDto.builder().fileName("filename1").description("описание файла 1").build(),
                        AttachmentDto.builder().fileName("filename2").description("описание файла 2").build()))
                .build();

        byte[] pdfData = ukaseService.callPdfService(TEMPLATE_NAME, reportRequestDto);

        return createFile(pdfData);
    }

    private File createFile(byte[] fileByteContent) {

        var fileName = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE).replace(" ", "_");
        var ext = ".pdf";
        var file = new File(String.join("", exampleProperties.getStorage(), File.separator, fileName, ext));
        try {
            Files.write(file.toPath(), fileByteContent);

            log.info("Save file to {}", file.getAbsolutePath());

            tryExistsFile(file);

            return file;

        } catch (IOException ex) {
            throw new RuntimeException("Cannot upload file to server: " + file.getAbsolutePath(), ex);
        }
    }

    private void tryExistsFile(File file) {
        if (file.exists()) {
            log.info("File: {} has been successfully created",
                    file.getAbsolutePath());
        } else {
            log.error("File: {} could not be created!",
                    file.getAbsolutePath());
        }
    }

}

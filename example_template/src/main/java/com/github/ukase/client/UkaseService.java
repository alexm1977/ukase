package com.github.ukase.client;

import com.github.ukase.client.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class UkaseService {

    private static final MediaType RESOURCES = new MediaType(
            "text",
            "plain",
            StandardCharsets.UTF_8
    );

    private static final String API_PDF = "/api/pdf";
    private static final String API_ASYNC_PDF = "/api/async/pdf/bulk";
    private static final String API_ASYNC_STATUS = "/api/async/{0}/status/";
    private static final String API_ASYNC_RESULT = "/api/async/{0}/";
    private static final String API_TEMPLATE_UPLOAD = "/api/resources/upload/";

    private final RestTemplate restTemplate;
    private final ResourceLoader resourceLoader;

    private final String generatePdfUrl;
    private final String asyncSubmitPdfUrl;
    private final String asyncStatusUrl;
    private final String asyncResultUrl;
    private final String templatePostUrl;

    private Collection<Resource> resources = new ArrayList<>();


    public UkaseService(@Qualifier("ukaseRestTemplate") RestTemplate restTemplate,
                        ResourceLoader resourceLoader,
                        @Value("${ukase.url}") String url
    ) {
        this.resourceLoader = resourceLoader;
        this.restTemplate = restTemplate;
        generatePdfUrl = url + API_PDF;
        asyncSubmitPdfUrl = url + API_ASYNC_PDF;
        asyncStatusUrl = url + API_ASYNC_STATUS;
        asyncResultUrl = url + API_ASYNC_RESULT;
        templatePostUrl = url + API_TEMPLATE_UPLOAD;
    }

    @PostConstruct
    public void init() throws IOException {
        Resource[] resources = ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader)
                .getResources("classpath*:templates/**");

        for (Resource resource : resources) {
            if (resource.contentLength() > 0) {
                this.resources.add(resource);
            }
        }
        initTemplates();
    }

    private boolean initTemplates() {
        try {
            for (Resource resource : resources) {
                log.info("Loading Ukase template: {}", resource);
                String content = IOUtils
                        .toString(resource.getInputStream(), StandardCharsets.UTF_8);
                uploadTemplate(content, resource.getFilename());
            }
            return true;
        } catch (IOException e) {
            log.warn("Cannot read template resource", e);
        } catch (RestClientException re) {
            log.warn("Cannot upload template resource", re);
        }
        return false;
    }

    private void uploadTemplate(String content, String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("path", Collections.singletonList(path));
        headers.setContentType(RESOURCES);
        HttpEntity<?> entity = new HttpEntity<Object>(content, headers);
        restTemplate.postForEntity(templatePostUrl, entity, Object.class);
    }

    public byte[] callPdfService(String templateName, Object data) {
        UkasePayload request = new UkasePayload(templateName, data);

        int tries = 0;
        while (tries++ < 3) {
            try {
                ResponseEntity<byte[]> response = restTemplate
                        .postForEntity(generatePdfUrl, request, byte[].class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    return response.getBody();
                }
            } catch (RestClientException e) {
                if (initTemplates()) {
                    continue;
                } else {
                    break;
                }
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignore) {
                break;
            }
        }
        throw new IllegalStateException("Data wasn't rendered in 3 tries");
    }

    public String callAsyncPdfGeneration(String templateName, Object data) {
        UkasePayload request = new UkasePayload(templateName, data);
        int tries = 0;
        while (tries++ < 3) {
            try {
                ResponseEntity<String> response = restTemplate
                        .postForEntity(asyncSubmitPdfUrl, request, String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    return response.getBody();
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    if (initTemplates()) {
                        continue;
                    } else {
                        break;
                    }
                }
                throw e;
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignore) {
                break;
            }
        }
        throw new IllegalStateException("Data wasn't rendered in 3 tries");
    }

    public UkaseStatus getAsyncReportStatus(String id) {
        ReportStatusResponse response = restTemplate.getForObject(asyncStatusUrl, ReportStatusResponse.class, id);
        return EnumUtils.getByCode(UkaseStatus.class, response.getStatus());
    }

    public byte[] getAsyncReportResult(String id) {
        return restTemplate.getForObject(asyncResultUrl, byte[].class, id);
    }
}

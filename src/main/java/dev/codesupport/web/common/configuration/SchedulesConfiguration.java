package dev.codesupport.web.common.configuration;

import dev.codesupport.web.api.service.FileServiceImpl;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.service.http.client.HttpClient;
import dev.codesupport.web.common.service.http.client.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableScheduling
@Slf4j
public class SchedulesConfiguration {

    @Value("${healthcheck.ping.url:}")
    private String healthcheckUrl;

    private Map<Integer, Boolean> memoryThresholds;

    private final HttpClient httpClient;

    public SchedulesConfiguration(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.memoryThresholds = new ConcurrentHashMap<>();
        memoryThresholds.put(20, false);
        memoryThresholds.put(15, false);
        memoryThresholds.put(10, false);
        memoryThresholds.put(5, false);
    }

    @Scheduled(fixedDelayString = "${healthcheck.ping.delay:1000}")
    public void scheduleHealthCheckPings() {
        if (!StringUtils.isEmpty(healthcheckUrl)) {
            httpClient.rest(null, String.class)
                    .withUrl(healthcheckUrl)
                    .sync(HttpMethod.GET);
        } else {
            throw new ConfigurationException("HealthCheck url not set");
        }
    }

    @Scheduled(initialDelay = 60000, fixedDelay = 30000)
    public void scheduleMemoryCheck() {
        double freeMemory = (double)Runtime.getRuntime().freeMemory() / Runtime.getRuntime().totalMemory();
        int freePercent = (int)(freeMemory * 100);
        for (Map.Entry<Integer, Boolean> threshold : memoryThresholds.entrySet()) {
            if (freePercent < threshold.getKey()) {
                if (Boolean.FALSE.equals(threshold.getValue())) {
                    threshold.setValue(true);
                    log.warn("Memory below " + threshold.getKey() + "% threshold.");
                }
            } else {
                if (Boolean.TRUE.equals(threshold.getValue())) {
                    threshold.setValue(false);
                }
            }
        }
    }

    @Scheduled(initialDelay = 30000, fixedDelay = 1800000)
    public void scheduleFileCleanup() throws IOException {
        File imageDir = new File(FileServiceImpl.IMAGE_STORAGE_DIR);
        if (imageDir.exists() && imageDir.isDirectory()) {
            for( File file : imageDir.listFiles() ) {
                if (file.isFile()) {
                    File image = new File(file.getAbsolutePath());
                    Files.probeContentType(image.toPath());
//                    Files.readAttributes(image.toPath(), "")
                }
            }
        }
    }

}

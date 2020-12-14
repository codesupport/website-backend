package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.service.http.client.HttpClient;
import dev.codesupport.web.common.service.http.client.HttpMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class SchedulesConfiguration {

    @Value("${healthcheck.ping.url:}")
    private String healthcheckUrl;

    private final HttpClient httpClient;

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
        if (freeMemory < 0.25) {
            log.warn("Running low on memory: " + ((int)(freeMemory * 100)) + "%");
        }
    }

}

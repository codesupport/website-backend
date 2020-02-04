package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.domain.OkResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HealthCheckControllerTest {

    @Test
    public void shouldReturnNullResponseForHealthCheck() {
        HealthCheckController controller = new HealthCheckController();

        OkResponse expected = new OkResponse();
        OkResponse actual = controller.getHealthCheck();

        assertEquals(expected, actual);
    }

}

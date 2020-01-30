package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.service.service.RestResponse;
import org.junit.Test;

import java.io.Serializable;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class HealthCheckControllerTest {

    @Test
    public void shouldReturnNullResponseForHealthCheck() {
        HealthCheckController controller = new HealthCheckController();

        RestResponse<Serializable> actual = controller.getHealthCheck();

        assertEquals(Collections.emptyList(), actual.getResponse());
    }

}

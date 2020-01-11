package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.service.service.RestResponse;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertNull;

public class HealthcheckControllerTest {

    @Test
    public void shouldReturnNullResponseForHealthcheck() {
        HealthcheckController controller = new HealthcheckController();

        RestResponse<Serializable> actual = controller.getHealthCheck();

        assertNull(actual.getResponse());
    }

}

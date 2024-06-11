package com.atauchi.transformerFileService.utilities.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HealthCheckConstantsTests {
    @Test
    fun `should verify url api for health check system`() {
        assertThat(HealthCheckConstants.BASE_URL).isEqualTo("")
        assertThat(HealthCheckConstants.GET_HEALTH_CHECK_URL).isEqualTo("/healthcheck")
    }
}

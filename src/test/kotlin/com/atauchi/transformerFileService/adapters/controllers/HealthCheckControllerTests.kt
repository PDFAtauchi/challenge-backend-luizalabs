package com.atauchi.transformerFileService.adapters.controllers

import com.atauchi.transformerFileService.utilities.constants.HealthCheckConstants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class HealthCheckControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var healthCheckUrl: String

    @BeforeEach
    fun setUp() {
        healthCheckUrl = HealthCheckConstants.BASE_URL + HealthCheckConstants.GET_HEALTH_CHECK_URL
    }

    @Test
    fun `should check health of the system is ok`() {
        // When
        val response: ResultActions = mockMvc.perform(get(healthCheckUrl))

        // Then
        response.andExpect(status().isOk)
    }
}
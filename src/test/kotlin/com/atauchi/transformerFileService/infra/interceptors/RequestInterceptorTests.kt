package com.atauchi.transformerFileService.infra.interceptors

import com.atauchi.transformerFileService.utilities.constants.HealthCheckConstants
import com.atauchi.transformerFileService.utilities.constants.UuidGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension::class)
@ExtendWith(OutputCaptureExtension::class)
@AutoConfigureMockMvc
class RequestInterceptorTests {
    @MockBean
    lateinit var uuidGenerator: UuidGenerator

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var healthCheckUrl: String

    @BeforeEach
    fun setUp() {
        healthCheckUrl = HealthCheckConstants.BASE_URL + HealthCheckConstants.GET_HEALTH_CHECK_URL
    }

    @Test
    fun `should generate log info with a request id in request and response`(output: CapturedOutput) {
        // Given it is generate a request di
        val mockUuid: String = "0001"
        `when`(uuidGenerator.generate()).thenReturn(mockUuid)

        // When
        val response: ResultActions = mockMvc.perform(get(healthCheckUrl))

        // Then
        val expectedRequestLog = "Incoming request requestId=$mockUuid method=GET uri=/healthcheck"
        val expectedResponseLog = "Completed request requestId=$mockUuid status=200"
        response.andExpect(status().isOk)
        assertTrue(output.out.contains(expectedRequestLog))
        assertTrue(output.out.contains(expectedResponseLog))
    }

    @Test
    fun `should check MDC requestId value in response`() {
        // Given it is generate a request di
        val mockUuid: String = "0001"
        `when`(uuidGenerator.generate()).thenReturn(mockUuid)

        // When
        val response: ResultActions = mockMvc.perform(get(healthCheckUrl))

        // Then after response is removed requestId from MDC
        response.andExpect(status().isOk)
        assertNull(MDC.get("requestId"))
    }
}

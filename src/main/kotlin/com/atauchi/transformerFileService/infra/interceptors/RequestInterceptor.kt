package com.atauchi.transformerFileService.infra.interceptors

import com.atauchi.transformerFileService.utilities.constants.UuidGenerator
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception

@Component
class RequestInterceptor : HandlerInterceptor {
    private val logger: Logger = LoggerFactory.getLogger(RequestInterceptor::class.java)

    @Autowired
    private lateinit var uuidGenerator: UuidGenerator

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val requestId: String = uuidGenerator.generate()
        MDC.put("requestId", requestId)
        logIncomingRequestDetails(request, requestId)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        val requestId: String = MDC.get("requestId") ?: "N/A"
        logCompletedRequestDetails(response, requestId)
        MDC.remove("requestId")
    }

    private fun logIncomingRequestDetails(
        request: HttpServletRequest,
        requestId: String,
    ) {
        logger.info("Incoming request requestId=$requestId method=${request.method} uri=${request.requestURI}")
    }

    private fun logCompletedRequestDetails(
        response: HttpServletResponse,
        requestId: String,
    ) {
        logger.info("Completed request requestId=$requestId status=${response.status}")
    }
}

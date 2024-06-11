package com.atauchi.transformerFileService.adapters.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @GetMapping("/healthcheck")
    fun getHealthCheck(): ResponseEntity<String> {
        return ResponseEntity<String>("{message: OK}", HttpStatus.OK)
    }
}

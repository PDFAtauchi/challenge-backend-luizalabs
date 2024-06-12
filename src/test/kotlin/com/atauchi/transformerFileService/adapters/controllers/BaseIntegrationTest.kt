package com.atauchi.transformerFileService.adapters.controllers

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
abstract class BaseIntegrationTest {
    companion object {
        @Container
        private val mongoDBContainer = MongoDBContainer("mongo:latest")

        @BeforeAll
        @JvmStatic
        fun startContainer() {
            mongoDBContainer.start()
        }

        @AfterAll
        @JvmStatic
        fun stopContainer() {
            mongoDBContainer.stop()
        }

        @DynamicPropertySource
        @JvmStatic
        fun mongoDbProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl)
        }
    }
}

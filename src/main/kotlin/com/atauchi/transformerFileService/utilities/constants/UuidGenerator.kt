package com.atauchi.transformerFileService.utilities.constants

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UuidGenerator {
    fun generate(): String {
        return UUID.randomUUID().toString()
    }
}

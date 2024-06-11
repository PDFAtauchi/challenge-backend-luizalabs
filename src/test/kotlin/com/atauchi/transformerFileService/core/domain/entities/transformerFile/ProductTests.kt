package com.atauchi.transformerFileService.core.domain.entities.transformerFile

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProductTests {
    @Test
    fun `should verify the store of Product information`() {
        // Given
        val productId: Int = 1
        val value: Double = 100.99

        // When
        val product: Product = Product(product_id = productId, value = value)

        // Then
        assertEquals(productId, product.product_id)
        assertEquals(value, product.value)
    }
}

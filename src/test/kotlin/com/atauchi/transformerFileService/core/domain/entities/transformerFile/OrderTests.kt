package com.atauchi.transformerFileService.core.domain.entities.transformerFile

import org.instancio.Instancio
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class OrderTests {
    @Test
    fun `should verify the store of Order information`() {
        // Given
        val orderId: Long = 1
        val date = "20240131"
        val total: String = "100.99"
        val products: List<Product> = Instancio.createList(Product::class.java)

        // When
        val order: Order = Order(order_id = orderId, date = date, total = total, products = products)

        // Then
        assertEquals(orderId, order.order_id)
        assertEquals(date, order.date)
        assertEquals(total, order.total)
        assertEquals(products, order.products)
    }
}

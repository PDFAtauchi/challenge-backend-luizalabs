package com.atauchi.transformerFileService.core.domain.entities.transformerFile

import org.instancio.Instancio
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserTests {
    @Test
    fun `should verify the store of User information`() {
        // Given
        val userId: Long = 1
        val name = "bob"
        val orders: List<Order> = Instancio.createList(Order::class.java)

        // When
        val user: User = User(user_id = userId, name = name, orders = orders)

        // Then
        assertEquals(userId, user.user_id)
        assertEquals(name, user.name)
        assertEquals(orders, user.orders)
    }
}

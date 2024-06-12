package com.atauchi.transformerFileService.core.application.orders

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.Order
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.Product
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import com.atauchi.transformerFileService.infra.db.mongo.FileDocument
import com.atauchi.transformerFileService.infra.db.mongo.TransformerFileRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class OrderQueryServiceTests {
    @Mock
    private lateinit var transformerFileRepository: TransformerFileRepository

    @InjectMocks
    private lateinit var orderQueryService: OrderQueryService

    val user1 =
        User(
            user_id = 1,
            name = "Masmer Prosacco",
            orders =
                listOf(
                    Order(
                        order_id = 234,
                        date = "2021-03-08",
                        total = "1836.74",
                        products = listOf(Product(product_id = 3, value = "1836.74")),
                    ),
                ),
        )

    val user2 =
        User(
            user_id = 2,
            name = "Babbie Botz",
            orders =
                listOf(
                    Order(
                        order_id = 123,
                        date = "2021-11-16",
                        total = "1578.57",
                        products = listOf(Product(product_id = 2, value = "1578.57")),
                    ),
                ),
        )

    val user3 =
        User(
            user_id = 3,
            name = "abc. Elaphem Trantow",
            orders =
                listOf(
                    Order(
                        order_id = 121,
                        date = "2021-11-27",
                        total = "1388.77",
                        products =
                            listOf(
                                Product(product_id = 6, value = "1288.77"),
                                Product(product_id = 6, value = "100.00"),
                            ),
                    ),
                    Order(
                        order_id = 125,
                        date = "2021-11-27",
                        total = "961.37",
                        products = listOf(Product(product_id = 7, value = "961.37")),
                    ),
                ),
        )

    @Test
    fun `should return all the orders`() {
        // Given
        val users = listOf(user1, user2, user3)
        val expectedOrders = users.flatMap { it.orders }
        val fileDocument: FileDocument = FileDocument(id = "001", checksum = "abc", data = users)
        given(transformerFileRepository.findAll()).willReturn(listOf(fileDocument))

        // When
        val actualOrders: List<Order> = orderQueryService.findOrdersBy(null, null, null)

        // Then
        assertEquals(expectedOrders, actualOrders)
    }

    @Test
    fun `should return a list of orders filter by order id`() {
        // Given
        val orderIdQuery: Long = 234L
        val users = listOf(user1, user2, user3)
        var expectedOrders =
            users.flatMap { user ->
                user.orders.filter { it.order_id == orderIdQuery }
            }

        val fileDocument: FileDocument = FileDocument(id = "001", checksum = "abc", data = users)
        given(transformerFileRepository.findAll()).willReturn(listOf(fileDocument))

        // When
        val actualOrders: List<Order> = orderQueryService.findOrdersBy(orderIdQuery, null, null)

        // Then
        assertEquals(expectedOrders, actualOrders)
    }

    @Test
    fun `should return a list of orders with date great than`() {
        // Given
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate: String? = "2012-08-31"
        val startDateFormatted = startDate?.let { LocalDate.parse(it, formatter) }

        val users = listOf(user1, user2, user3)
        var expectedOrders =
            users.flatMap { user ->
                user.orders.filter { order ->
                    startDateFormatted == null || LocalDate.parse(order.date, formatter).isAfter(startDateFormatted.minusDays(1))
                }
            }

        val fileDocument: FileDocument = FileDocument(id = "001", checksum = "abc", data = users)
        given(transformerFileRepository.findAll()).willReturn(listOf(fileDocument))

        // When
        val actualOrders: List<Order> = orderQueryService.findOrdersBy(null, startDate, null)

        // Then
        assertEquals(expectedOrders, actualOrders)
    }
}

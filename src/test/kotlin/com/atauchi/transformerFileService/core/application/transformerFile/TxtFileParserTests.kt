package com.atauchi.transformerFileService.core.application.transformerFile

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.Order
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.Product
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import com.atauchi.transformerFileService.core.domain.useCases.transformerFile.FileParser
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.File
import kotlin.test.assertEquals

class TxtFileParserTests {
    private lateinit var file: MultipartFile

    private lateinit var txtFileParser: FileParser

    @BeforeEach
    fun setUp() {
        txtFileParser = TxtFileParser()
    }

    fun sortUsers(users: List<User>): List<User> {
        val sortedUsers = users.sortedWith(compareBy({ it.user_id }, { it.name }))

        val usersWithSortedOrders =
            sortedUsers.map { user ->
                user.copy(orders = user.orders.sortedWith(compareBy({ it.order_id }, { it.total.toDouble() })))
            }

        val usersWithSortedOrdersAndProducts =
            usersWithSortedOrders.map { user ->
                user.copy(
                    orders =
                        user.orders.map { order ->
                            order.copy(products = order.products.sortedWith(compareBy({ it.product_id }, { it.value.toDouble() })))
                        },
                )
            }

        return usersWithSortedOrdersAndProducts
    }

    @Test
    fun `should parse file and return list of users`() {
        // Given a valid txt file to be parsed
        val path = "src/test/kotlin/com/atauchi/transformerFileService/mockdata/order_data.txt"
        val fileContent = File(path).readBytes()
        file =
            MockMultipartFile(
                "testfile.txt",
                "testfile.txt",
                "text/plain",
                fileContent,
            )

        // and the expected users
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

        var expectedUsers = listOf(user1, user2, user3)
        expectedUsers = sortUsers(expectedUsers)

        // When
        var usersParsed: List<User> = txtFileParser.parse(file)
        usersParsed = sortUsers(usersParsed)

        // Then
        assertEquals(expectedUsers, usersParsed)
    }

    @Test
    fun `should throw an exception when parse file`() {
        // Given wrong order data
        val path = "src/test/kotlin/com/atauchi/transformerFileService/mockdata/wrong_order_data.txt"
        val fileContent = File(path).readBytes()
        file =
            MockMultipartFile(
                "testfile.txt",
                "testfile.txt",
                "text/plain",
                fileContent,
            )

        // When and Then
        assertThatThrownBy {
            txtFileParser.parse(file)
        }.isInstanceOf(Exception::class.java)
    }
}

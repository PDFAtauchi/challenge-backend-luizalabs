package com.atauchi.transformerFileService.core.application

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.Order
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.Product
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import com.atauchi.transformerFileService.core.domain.exceptions.file.ParseFileException
import com.atauchi.transformerFileService.core.domain.useCases.FileParser
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class TxtFileParser : FileParser {
    override fun parse(file: MultipartFile): List<User> {
        var br: BufferedReader? = null
        var usersMap = mutableMapOf<Long, User>()
        var ordersMap = mutableMapOf<Long, MutableList<Order>>()

        try {
            br = BufferedReader(InputStreamReader(file.inputStream))
            br.useLines { lines ->
                lines.forEach { line ->
                    val userId: Long = line.substring(0, 10).trim().toLong()
                    val name: String = line.substring(10, 55).trim()
                    var orderId: Long = line.substring(55, 65).trim().toLong()
                    var productId: Int = line.substring(65, 75).trim().toInt()
                    var price: String = line.substring(75, 87).trim()

                    var date: String = line.substring(87, 95).trim()
                    date = date.substring(0..3) + "-" + date.substring(4..5) + "-" + date.substring(6..7)

                    var product = Product(product_id = productId, value = price)

                    var order =
                        ordersMap.getOrPut(orderId) {
                            mutableListOf(Order(order_id = orderId, date = date, total = "0.0", products = mutableListOf()))
                        }.first()
                    order.products += product
                    order.total = "%.2f".format((order.total).toDouble() + price.toDouble())

                    val user = usersMap.getOrPut(userId) { User(user_id = userId, name = name, orders = mutableListOf()) }
                    if (order !in user.orders) {
                        user.orders += order
                    }
                }
            }
        } catch (e: IOException) {
            throw ParseFileException("Error reading file")
        } catch (e: Exception) {
            throw Exception("Error parsing file data")
        } finally {
            br?.close()
        }

        return usersMap.values.toList()
    }
}

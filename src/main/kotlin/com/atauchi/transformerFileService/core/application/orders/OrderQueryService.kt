package com.atauchi.transformerFileService.core.application.orders

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.Order
import com.atauchi.transformerFileService.core.domain.useCases.orders.OrderQueryUseCase
import com.atauchi.transformerFileService.infra.db.mongo.TransformerFileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class OrderQueryService : OrderQueryUseCase {
    @Autowired
    lateinit var transformerFileRepository: TransformerFileRepository

    override fun findOrdersBy(
        orderId: Long?,
        startDate: String?,
        endDate: String?,
    ): List<Order> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = startDate?.let { LocalDate.parse(it, formatter) }
        val end = endDate?.let { LocalDate.parse(it, formatter) }

        return transformerFileRepository.findAll().flatMap { file ->
            file.data.flatMap { user ->
                user.orders.filter { order ->
                    (
                        (orderId == null || order.order_id == orderId) &&
                            (start == null || LocalDate.parse(order.date, formatter).isAfter(start.minusDays(1))) &&
                            (end == null || LocalDate.parse(order.date, formatter).isBefore(end.plusDays(1)))
                    )
                }
            }
        }
    }
}

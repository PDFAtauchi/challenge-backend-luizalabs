package com.atauchi.transformerFileService.core.domain.useCases.orders

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.Order

interface OrderQueryUseCase {
    fun findOrdersBy(
        orderId: Long?,
        startDate: String?,
        endDate: String?,
    ): List<Order>
}

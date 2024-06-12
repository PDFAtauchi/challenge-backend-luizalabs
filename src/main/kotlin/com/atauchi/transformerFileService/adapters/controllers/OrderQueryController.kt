package com.atauchi.transformerFileService.adapters.controllers

import com.atauchi.transformerFileService.core.application.orders.OrderQueryService
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.Order
import com.atauchi.transformerFileService.utilities.constants.OrderQueryConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(OrderQueryConstants.BASE_URL)
class OrderQueryController {
    @Autowired
    private lateinit var orderQueryService: OrderQueryService

    @GetMapping(OrderQueryConstants.GET_ORDER_QUERY_BY_FILTERS_URL)
    fun transformFile(
        @RequestParam(required = false) orderId: Long?,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?,
    ): ResponseEntity<List<Order>> {
        try {
            val orders = orderQueryService.findOrdersBy(orderId, startDate, endDate)
            return ResponseEntity.ok(orders)
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(listOf())
        }
    }
}

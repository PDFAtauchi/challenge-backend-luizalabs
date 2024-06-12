package com.atauchi.transformerFileService.core.domain.entities.transformerFile

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val order_id: Long,
    val date: String,
    var total: String,
    var products: List<Product>,
)

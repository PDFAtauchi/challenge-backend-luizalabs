package com.atauchi.transformerFileService.core.domain.entities.transformerFile

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val product_id: Int,
    val value: Double,
)

package com.atauchi.transformerFileService.core.domain.entities.transformerFile

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_id: Long,
    val name: String,
    var orders: List<Order>,
)

package com.atauchi.transformerFileService.infra.db.mongo

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "files")
@Serializable
data class FileDocument(
    @Id val id: String? = null,
    val checksum: String,
    val data: List<User>,
)

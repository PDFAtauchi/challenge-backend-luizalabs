package com.atauchi.transformerFileService.infra.db.mongo

import org.springframework.data.mongodb.repository.MongoRepository

interface TransformerFileRepository : MongoRepository<FileDocument, String> {
    fun findByChecksum(checksum: String): FileDocument?
}

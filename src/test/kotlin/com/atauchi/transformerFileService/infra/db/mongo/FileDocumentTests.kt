package com.atauchi.transformerFileService.infra.db.mongo

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import org.instancio.Instancio
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FileDocumentTests {
    @Test
    fun `should verify the store of FileDocument information`() {
        // Given
        val fileDocumentId: String = "1"
        val checksum: String = "0001"
        var data: List<User> = Instancio.createList(User::class.java)

        // When
        val fileDocument: FileDocument = FileDocument(id = fileDocumentId, checksum = checksum, data = data)

        // Then
        assertEquals(fileDocumentId, fileDocument.id)
        assertEquals(checksum, fileDocument.checksum)
        assertEquals(
            data,
            fileDocument.data,
        )
    }
}

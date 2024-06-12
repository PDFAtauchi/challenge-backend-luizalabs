package com.atauchi.transformerFileService.core.application

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.File
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import com.atauchi.transformerFileService.infra.db.mongo.FileDocument
import com.atauchi.transformerFileService.infra.db.mongo.TransformerFileRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.web.multipart.MultipartFile
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class TransformerFileServiceTests {
    @Mock
    private lateinit var transformerFileRepository: TransformerFileRepository

    @Mock
    private lateinit var txtFileParser: TxtFileParser

    @InjectMocks
    private lateinit var transformerFileService: TransformerFileService

    private lateinit var file: MultipartFile

    private lateinit var users: List<User>

    @BeforeEach
    fun setUp() {
        file = mock(MultipartFile::class.java)
        users = listOf(User(user_id = 1, name = "bob", listOf()))
    }

    @Test
    fun `should retrieved file data when already exist file in database`() {
        // Given
        val mockFile: File = mock<File>()
        whenever(file.size).thenReturn(4 * 1024 * 1024)
        whenever(file.contentType).thenReturn("text/plain")
        whenever(file.isEmpty).thenReturn(false)
        whenever(file.bytes).thenReturn("fake file content".toByteArray())

        val checksum = "3bf637408200bd7f04873f03a7aa5c97a792e2c9c89ce6b5d688fbc8353edfeb"
        val fileDocumentExpected: FileDocument? = FileDocument(checksum = checksum, data = users)
        given(transformerFileRepository.findByChecksum(checksum)).willReturn(fileDocumentExpected)

        // When
        val usersRetrieved: List<User> = transformerFileService.save(file)

        // Then
        assertEquals(users, usersRetrieved)
    }

    @Test
    fun `should save file and return file data`() {
        // Given
        val mockFile: File = mock<File>()
        whenever(file.size).thenReturn(4 * 1024 * 1024)
        whenever(file.contentType).thenReturn("text/plain")
        whenever(file.isEmpty).thenReturn(false)
        whenever(file.bytes).thenReturn("fake file content".toByteArray())

        val checksum = "3bf637408200bd7f04873f03a7aa5c97a792e2c9c89ce6b5d688fbc8353edfeb"
        val fileDocumentExpected: FileDocument? = FileDocument(checksum = checksum, data = users)
        given(txtFileParser.parse(file)).willReturn(users)
        given(transformerFileRepository.save(fileDocumentExpected!!)).willReturn(fileDocumentExpected)

        // When
        val usersRetrieved: List<User> = transformerFileService.save(file)

        // Then
        assertEquals(users, usersRetrieved)
    }

    @Test
    fun `should throw exception when invalid data file`() {
        // When and Then
        assertThatThrownBy {
            transformerFileService.save(file)
        }.isInstanceOf(Exception::class.java)
    }
}

package com.atauchi.transformerFileService.core.domain.entities.transformerFile

import com.atauchi.transformerFileService.core.domain.exceptions.file.FileException
import com.atauchi.transformerFileService.core.domain.exceptions.file.ParseFileException
import com.atauchi.transformerFileService.core.domain.useCases.transformerFile.FileParser
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.doReturn
import org.mockito.BDDMockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.`when`
import org.springframework.web.multipart.MultipartFile
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileTests {
    lateinit var file: MultipartFile

    @BeforeEach
    fun setUp() {
        file = mock(MultipartFile::class.java)
    }

    @Nested
    inner class FileTypeTests {
        @Test
        fun `should verify file type is text plain`() {
            // Given
            `when`(file.contentType).thenReturn("text/plain")
            val fileParser = mock(FileParser::class.java)

            // When
            val myfile = File(file, fileParser)

            // Then
            assertTrue(myfile.isValidFileType())
        }

        @Test
        fun `should throw exception when invalid type`() {
            // Given
            `when`(file.contentType).thenReturn("json")
            val fileParser = mock(FileParser::class.java)

            // When and Then
            assertThatThrownBy {
                File(file, fileParser)
            }.isInstanceOf(FileException::class.java)
        }
    }

    @Nested
    inner class FileIsEmptyTests {
        @Test
        fun `should verify file is not empty`() {
            // Given
            `when`(file.contentType).thenReturn("text/plain")
            `when`(file.isEmpty).thenReturn(false)
            val fileParser = mock(FileParser::class.java)

            // When
            val myfile = File(file, fileParser)

            // Then
            assertFalse(myfile.isEmpty())
        }

        @Test
        fun `should throw exception when file is empty`() {
            // Given
            `when`(file.contentType).thenReturn("text/plain")
            `when`(file.isEmpty).thenReturn(true)
            val fileParser = mock(FileParser::class.java)

            // When and Then
            assertThatThrownBy {
                File(file, fileParser)
            }.isInstanceOf(FileException::class.java)
        }
    }

    @Nested
    inner class FileSizeTests {
        @Test
        fun `should verify size file not exceeds max size file`() {
            // Given
            `when`(file.contentType).thenReturn("text/plain")
            `when`(file.isEmpty).thenReturn(false)
            `when`(file.size).thenReturn(4 * 1024 * 1024)
            val fileParser = mock(FileParser::class.java)

            // When
            val myfile = File(file, fileParser)

            // Then
            assertEquals(myfile.getSize(), 4 * 1024 * 1024)
        }

        @Test
        fun `should throw exception when file exceeds max size file`() {
            // Given
            `when`(file.contentType).thenReturn("text/plain")
            `when`(file.isEmpty).thenReturn(false)
            `when`(file.size).thenReturn(6 * 1024 * 1024)
            val fileParser = mock(FileParser::class.java)

            // When and Then
            assertThatThrownBy {
                File(file, fileParser)
            }.isInstanceOf(FileException::class.java)
        }
    }

    @Nested
    inner class FileGetChecksumTests {
        @Test
        fun `should return checksum successful`() {
            // Given
            `when`(file.contentType).thenReturn("text/plain")
            `when`(file.isEmpty).thenReturn(false)
            `when`(file.size).thenReturn(4 * 1024 * 1024)
            val fileParser = mock(FileParser::class.java)
            val myfile = File(file, fileParser)

            val spyFile = spy(myfile)
            doReturn("ABC").`when`(spyFile).getChecksum()

            // When
            val checksum: String = spyFile.getChecksum()

            // Then
            assertEquals("ABC", checksum)
        }

        @Test
        fun `should throw exception when error getting file checksum`() {
            // Given
            `when`(file.contentType).thenReturn("text/plain")
            `when`(file.isEmpty).thenReturn(false)
            `when`(file.size).thenReturn(4 * 1024 * 1024)
            val fileParser = mock(FileParser::class.java)
            val myfile = File(file, fileParser)

            val spyFile = spy(myfile)
            doThrow(FileException("abc")).`when`(spyFile).getChecksum()

            // When and Then
            assertThatThrownBy {
                spyFile.getChecksum()
            }.isInstanceOf(FileException::class.java)
        }
    }

    @Nested
    inner class FileParseTests {
        @Test
        fun `should return user lists when parse file successful`() {
            // Given
            `when`(file.contentType).thenReturn("text/plain")
            `when`(file.isEmpty).thenReturn(false)
            `when`(file.size).thenReturn(4 * 1024 * 1024)
            val fileParser = mock(FileParser::class.java)
            val myfile = File(file, fileParser)

            val spyFile = spy(myfile)
            val usersDummy: List<User> = listOf()
            doReturn(usersDummy).`when`(spyFile).parseFile()

            // When
            val usersParsed: List<User> = spyFile.parseFile()

            // Then
            assertEquals(usersDummy, usersParsed)
        }

        @Test
        fun `should throw exception when error parsing file`() {
            // Given
            `when`(file.contentType).thenReturn("text/plain")
            `when`(file.isEmpty).thenReturn(false)
            `when`(file.size).thenReturn(4 * 1024 * 1024)
            val fileParser = mock(FileParser::class.java)
            val myfile = File(file, fileParser)

            val spyFile = spy(myfile)
            doThrow(ParseFileException("ABC")).`when`(spyFile).parseFile()

            // When and Then
            assertThatThrownBy {
                spyFile.parseFile()
            }.isInstanceOf(ParseFileException::class.java)
        }
    }
}

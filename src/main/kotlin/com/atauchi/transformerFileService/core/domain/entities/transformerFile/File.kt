package com.atauchi.transformerFileService.core.domain.entities.transformerFile

import com.atauchi.transformerFileService.core.domain.exceptions.file.FileChecksumOperationException
import com.atauchi.transformerFileService.core.domain.exceptions.file.FileEmptyException
import com.atauchi.transformerFileService.core.domain.exceptions.file.FileSizeException
import com.atauchi.transformerFileService.core.domain.exceptions.file.FileTypeException
import com.atauchi.transformerFileService.core.domain.useCases.FileParser
import org.springframework.web.multipart.MultipartFile
import java.security.MessageDigest

class File(
    private val file: MultipartFile,
    private val fileParser: FileParser,
) {
    companion object {
        const val MAX_FILE_SIZE = 5 * 1024 * 1024
    }

    init {
        if (!isValidFileType()) {
            throw FileTypeException("Invalid file type, it needs to be .txt type")
        }

        if (isEmpty()) {
            throw FileEmptyException("Invalid empty file, it can not be empty")
        }

        if (getSize() > MAX_FILE_SIZE) {
            throw FileSizeException("File can not exceeds 5MB")
        }
    }

    fun isValidFileType(): Boolean {
        return file.contentType == "text/plain"
    }

    fun isEmpty(): Boolean {
        return file.isEmpty
    }

    fun getSize(): Long {
        return file.size
    }

    fun getChecksum(): String {
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = file.bytes
            val hash = digest.digest(bytes)
            return hash.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            throw FileChecksumOperationException("Error getting the checksum of the file")
        }
    }

    fun parseFile(): List<User> {
        return fileParser.parse(file)
    }
}

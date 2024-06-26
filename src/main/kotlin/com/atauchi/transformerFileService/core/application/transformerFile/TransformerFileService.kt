package com.atauchi.transformerFileService.core.application.transformerFile

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.File
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import com.atauchi.transformerFileService.core.domain.exceptions.file.FileException
import com.atauchi.transformerFileService.core.domain.exceptions.file.ParseFileException
import com.atauchi.transformerFileService.core.domain.useCases.transformerFile.TransformerFileSaveUseCase
import com.atauchi.transformerFileService.infra.db.mongo.FileDocument
import com.atauchi.transformerFileService.infra.db.mongo.TransformerFileRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class TransformerFileService : TransformerFileSaveUseCase {
    private val objectMarker = jacksonObjectMapper()

    @Autowired
    private lateinit var txtFileParser: TxtFileParser

    @Autowired
    private lateinit var transformerFileRepository: TransformerFileRepository

    override fun save(file: MultipartFile): List<User> {
        try {
            var users = findByCheckSum(file)
            if (users != null) return users

            users = saveFileInDB(file)
            return users
        } catch (e: FileException) {
            throw FileException("${e.message}")
        } catch (e: ParseFileException) {
            throw ParseFileException("${e.message}")
        } catch (e: Exception) {
            throw Exception("${e.message}")
        }
    }

    private fun findByCheckSum(file: MultipartFile): List<User>? {
        val checksum = File(file = file, fileParser = txtFileParser).getChecksum()
        return transformerFileRepository.findByChecksum(checksum)?.data
    }

    private fun saveFileInDB(file: MultipartFile): List<User> {
        val fileData = File(file = file, fileParser = txtFileParser)
        val parsedFile: List<User> = fileData.parseFile()
        val checksum = fileData.getChecksum()
        val fileDocument = FileDocument(checksum = checksum, data = parsedFile)
        val fileDocumentSaved = transformerFileRepository.save(fileDocument)
        return fileDocumentSaved.data
    }
}

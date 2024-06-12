package com.atauchi.transformerFileService.adapters.controllers

import com.atauchi.transformerFileService.core.application.transformerFile.TransformerFileService
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import com.atauchi.transformerFileService.core.domain.exceptions.file.FileException
import com.atauchi.transformerFileService.core.domain.exceptions.file.ParseFileException
import com.atauchi.transformerFileService.utilities.constants.TransformerFileConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(TransformerFileConstants.BASE_URL)
class TransformerFileController {
    @Autowired
    private lateinit var transformerFileService: TransformerFileService

    @PostMapping(TransformerFileConstants.POST_TRANSFORM_FILE_URL)
    fun transformFile(
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<*> {
        try {
            val users = transformerFileService.save(file)
            return ResponseEntity<List<User>>(users, HttpStatus.CREATED)
        } catch (e: FileException) {
            return ResponseEntity.badRequest().body(mapOf("message" to "${e.message}"))
        } catch (e: ParseFileException) {
            return ResponseEntity.badRequest().body(mapOf("message" to "${e.message}"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(mapOf("message" to "${e.message}"))
        }
    }
}

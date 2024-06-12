package com.atauchi.transformerFileService.core.domain.useCases.transformerFile

import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import org.springframework.web.multipart.MultipartFile

interface TransformerFileSaveUseCase {
    fun save(file: MultipartFile): List<User>
}

package com.atauchi.transformerFileService.adapters.controllers

import com.atauchi.transformerFileService.core.application.TransformerFileService
import com.atauchi.transformerFileService.core.domain.entities.transformerFile.User
import com.atauchi.transformerFileService.utilities.constants.TransformerFileConstants
import com.atauchi.transformerFileService.utilities.constants.UuidGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.io.File
import kotlin.test.assertEquals

@AutoConfigureMockMvc
class TransformerFileControllerTests : BaseIntegrationTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var uuidGenerator: UuidGenerator

    @Autowired
    private lateinit var transformerFileService: TransformerFileService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var users: List<User>

    private lateinit var transformFileUrlApi: String

    @BeforeEach
    fun setUp() {
        transformFileUrlApi = TransformerFileConstants.BASE_URL + TransformerFileConstants.POST_TRANSFORM_FILE_URL
        users = Instancio.createList(User::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun `should transform sucessfull an denormalized order data file into a normalized JSON file`() {
        // Given
        val path = "src/test/kotlin/com/atauchi/transformerFileService/mockdata/order_data.txt"
        val fileContent = File(path).readBytes()
        val file: MockMultipartFile =
            MockMultipartFile(
                "file",
                "testfile.txt",
                "text/plain",
                fileContent,
            )

        // When
        val response: ResultActions =
            mockMvc.perform(
                MockMvcRequestBuilders.multipart(transformFileUrlApi)
                    .file(file)
                    .contentType(MediaType.MULTIPART_FORM_DATA),
            )
        val result = response.andReturn()

        // Then
        response.andExpect(MockMvcResultMatchers.status().isCreated)

        val objectMapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

        val responseData = result.response.contentAsString
        val jsonResponseData = objectMapper.readTree(responseData)
        val responseDataFormatted = objectMapper.writeValueAsString(jsonResponseData)

        val expectedData = File("src/test/kotlin/com/atauchi/transformerFileService/mockdata/order_data_normalized.json")
        val jsonExpectedData = objectMapper.readTree(expectedData.readText())
        val jsonExpectedDataFormatted = objectMapper.writeValueAsString(jsonExpectedData)

        assertEquals(jsonExpectedDataFormatted, responseDataFormatted)
    }

    @Test
    @Throws(Exception::class)
    fun `should return error when file contains error in data`() {
        // Given
        // Given
        val path = "src/test/kotlin/com/atauchi/transformerFileService/mockdata/wrong_order_data.txt"
        val fileContent = File(path).readBytes()
        val file: MockMultipartFile =
            MockMultipartFile(
                "file",
                "testfile.txt",
                "text/plain",
                fileContent,
            )

        // When
        val response: ResultActions =
            mockMvc.perform(
                MockMvcRequestBuilders.multipart(transformFileUrlApi)
                    .file(file)
                    .contentType(MediaType.MULTIPART_FORM_DATA),
            )
        val result = response.andReturn()

        // Then
        response.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}

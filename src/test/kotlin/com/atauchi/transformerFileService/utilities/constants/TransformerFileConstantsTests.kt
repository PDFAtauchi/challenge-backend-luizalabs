package com.atauchi.transformerFileService.utilities.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransformerFileConstantsTests {
    @Test
    fun `should verify url api for transform files`() {
        assertThat(TransformerFileConstants.BASE_URL).isEqualTo("/api/v1/files")
        assertThat(TransformerFileConstants.POST_TRANSFORM_FILE_URL).isEqualTo("/transform")
    }
}

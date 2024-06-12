package com.atauchi.transformerFileService.utilities.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderQueryConstantsTests {
    @Test
    fun `should verify url api for query orders`() {
        assertThat(OrderQueryConstants.BASE_URL).isEqualTo("/api/v1")
        assertThat(OrderQueryConstants.GET_ORDER_QUERY_BY_FILTERS_URL).isEqualTo("/orders/search")
    }
}

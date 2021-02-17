package com.descartes.extensions

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class StringTest {

    @Test
    fun `When get url without parameters returns clean url`() {
        val url = "https://stanete.com/system-design-101?utm_source=active%20users&utm_medium=email"

        url.withoutUrlParameters() shouldBeEqualTo "https://stanete.com/system-design-101"
    }
}

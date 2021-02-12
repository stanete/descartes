package com.descartes.actions

import com.descartes.mqtp.Message
import com.descartes.mqtp.Publisher
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class PublishMessageTest {

    private val publisher = mockk<Publisher>()
    private val publishMessage = PublishMessage(publisher)

    @Test
    fun `When invoking action sends message through publisher with message as string`() {
        val queueName = "queueName"
        val message = "{\"data\":{\"url\":\"https://stanete.com/system-design-101\"}}"
        justRun { publisher.send(queueName, message) }

        publishMessage(queueName, Message(data = mapOf("url" to "https://stanete.com/system-design-101")))

        verify { publisher.send(queueName, message) }
    }
}

package com.descartes.actions

import com.descartes.mqtp.Message
import com.descartes.mqtp.Publisher
import org.springframework.stereotype.Service

@Service
class PublishMessage(val publisher: Publisher) {

    operator fun invoke(queueName: String, message: Message) {
        publisher.send(queueName, message.toJson())
    }
}

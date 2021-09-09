package com.etaskify.ms.tasks.service

import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class NotifyService(
        private val rabbitTemplate: RabbitTemplate,
        @Value("\${queue.taskify-notify}")
        private val queueName: String
) {
    companion object {
        private val logger = LoggerFactory.getLogger(NotifyService::class.java)
    }

    fun notifyUser(userName: String, userSurname: String, taskName: String) {
        buildMessageAndNotify(userName, userSurname, taskName)
    }

    private fun buildMessageAndNotify(userName: String, userSurname: String, taskName: String) {
        val messageDto = CreateMessageDto(
                userName = userName,
                taskName = taskName,
                userSurname = userSurname

        )
        val message = Gson().toJson(messageDto)
        rabbitTemplate.convertAndSend(queueName, message)
    }

    data class CreateMessageDto(
            var userName: String,
            var taskName: String,
            var userSurname: String
    )

}
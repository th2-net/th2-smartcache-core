package com.exactpro.th2.smartcache.state.impl

import com.exactpro.th2.common.grpc.Message
import com.exactpro.th2.common.message.messageType
import com.exactpro.th2.common.message.sessionAlias
import com.exactpro.th2.read.file.common.AbstractFileReader
import com.exactpro.th2.smartcache.state.IState
import mu.KotlinLogging

abstract class SimpleReaderState : IState {
    private val logger = KotlinLogging.logger { this::class.simpleName }

    abstract val alias: String

    override fun onParsedMessage(message: Message) {
        if (message.sessionAlias != alias) {
            logger.trace { "${message.messageType} was passed due alias filter" }
            return
        }

        when (message.metadata.propertiesMap[AbstractFileReader.MESSAGE_STATUS_PROPERTY]) {
            AbstractFileReader.MESSAGE_STATUS_FIRST -> {
                logger.trace { "$alias: Received first" }
                handleFirst(message)
            }
            AbstractFileReader.MESSAGE_STATUS_LAST -> {
                logger.trace { "$alias: Received last" }
                handleLast(message)
            }
            AbstractFileReader.MESSAGE_STATUS_SINGLE -> {
                logger.trace { "$alias: Received single" }
                handleSingle(message)
            }
            else -> {
                logger.trace { "$alias: Received middle" }
                handleMiddle(message)
            }
        }
    }

    abstract fun handleFirst(message: Message)
    abstract fun handleMiddle(message: Message)
    abstract fun handleLast(message: Message)
    abstract fun handleSingle(message: Message)

}

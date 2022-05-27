package com.exactpro.th2.smartcache.state

import com.exactpro.th2.common.event.Event
import com.exactpro.th2.common.grpc.Message
import com.exactpro.th2.common.grpc.MessageGroup
import com.exactpro.th2.common.grpc.RawMessage
import mu.KotlinLogging

interface IState {

    val settingsClass: Class<out IStateManagerSettings>

    fun init(context: StateManagerContext, customSettings: IStateManagerSettings)

    fun onMessageGroup(messageGroup: MessageGroup) {
        LOGGER.trace { "Received message group" }
        messageGroup.messagesList.forEach {
            when {
                it.hasMessage() -> onParsedMessage(it.message)
                it.hasRawMessage() -> onRawMessage(it.rawMessage)
                else -> error("Unsupported type of message: ${it.kindCase}")
            }
        }
    }

    fun onParsedMessage(message: Message): Unit

    fun onRawMessage(message: RawMessage): Unit = error("Raw message unsupported for ${this::class.java} state")

    data class StateManagerContext(
        val parentEventId: String,
        val storeEvent: (event: Event, parentEventID: String) -> String
    )

    companion object {
        private val LOGGER = KotlinLogging.logger { IState::class.simpleName }
    }
}
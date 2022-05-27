package com.exactpro.th2.smartcache.store.impl

import com.exactpro.th2.common.grpc.EventBatch
import com.exactpro.th2.common.grpc.MessageGroupBatch
import com.exactpro.th2.common.schema.message.MessageRouter
import com.exactpro.th2.common.schema.message.storeEvent
import com.exactpro.th2.smartcache.Settings
import com.exactpro.th2.smartcache.state.IState
import com.exactpro.th2.smartcache.store.IStore
import com.exactpro.th2.smartcache.utils.createEvent
import mu.KotlinLogging

class ConfigStore(private val settings: Settings, private val rootEventId: String, private val eventRouter: MessageRouter<EventBatch>, private val manager: IState) : IStore {

    override fun onMessageGroupBatch(messageGroup: MessageGroupBatch) {
        LOGGER.trace { "Received message group batch" }
        messageGroup.groupsList.forEach { group ->
            try {
                manager.onMessageGroup(group)
            }  catch (e: Exception) {
                eventRouter.storeEvent(createEvent("Error during processing phase from manager, messageGroup: $group",  throwable = e), rootEventId)
            }
        }
    }

    override fun close() = Unit

    companion object {
        private val LOGGER = KotlinLogging.logger { ConfigStore::class.simpleName }
    }

}
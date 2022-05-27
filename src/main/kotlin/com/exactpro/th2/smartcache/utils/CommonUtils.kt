package com.exactpro.th2.smartcache.utils

import com.exactpro.th2.common.event.Event
import com.exactpro.th2.common.grpc.MessageID

fun createEvent(name: String, messagesId: List<MessageID> = emptyList(), throwable: Throwable? = null): Event {
    val type = if (throwable != null) "Error" else "Info"
    val status = if (throwable != null) Event.Status.FAILED else Event.Status.PASSED

    return Event.start().apply {
        endTimestamp()
        name(name)
        type(type)
        status(status)

        messagesId.forEach(this::messageID)
    }
}

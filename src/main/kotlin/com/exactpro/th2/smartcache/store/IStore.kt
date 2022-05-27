package com.exactpro.th2.smartcache.store

import com.exactpro.th2.common.grpc.MessageGroupBatch

interface IStore : AutoCloseable {
    fun onMessageGroupBatch(messageGroup: MessageGroupBatch)
}
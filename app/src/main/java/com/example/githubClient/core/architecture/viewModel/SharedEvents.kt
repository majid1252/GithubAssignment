package com.example.githubClient.core.architecture.viewModel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import java.util.concurrent.CopyOnWriteArraySet

interface SharedEvents<out T : ViewEvent> {
    fun stream(consumerId: String): Flow<T>
}

class EventQueue<T : ViewEvent>(capacity: Int) : SharedEvents<T> {

    private val innerQueue = MutableSharedFlow<OneTimeEvent<T>>(replay = capacity)

    fun post(event: T) {
        innerQueue.tryEmit(OneTimeEvent(event))
    }

    override fun stream(consumerId: String): Flow<T> = innerQueue
            .onEach {
                // Ensure that buffered Events will not be sent again to new subscribers.
                innerQueue.resetReplayCache()
            }
            .filterNotHandledBy(consumerId)
}

/**
 * Event designed to be delivered only once to a concrete entity,
 * but it can also be delivered to multiple different entities.
 *
 * Keeps track of who has already handled its content.
 */
private class OneTimeEvent<out T : ViewEvent>(private val content: T) {

    private val handlers = CopyOnWriteArraySet<String>()

    /**
     * @param asker Used to identify, whether this "asker" has already handled this Event.
     * @return Event content or null if it has been already handled by asker
     */
    fun getIfNotHandled(asker: String): T? = if (handlers.add(asker)) content else null
}

private fun <T : ViewEvent> Flow<OneTimeEvent<T>>.filterNotHandledBy(consumerId: String): Flow<T> = transform { event ->
    event.getIfNotHandled(consumerId)?.let { emit(it) }
}

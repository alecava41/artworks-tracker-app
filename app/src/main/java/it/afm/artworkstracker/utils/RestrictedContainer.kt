package it.afm.artworkstracker.utils

import java.util.Queue
import java.util.Stack
import java.util.concurrent.ArrayBlockingQueue

class RestrictedContainer<T> (private val size: Int) {
    private val realContainer = ArrayBlockingQueue<T>(size)

    fun push(item: T) {
        if (realContainer.size >= size) realContainer.remove()
        realContainer.add(item)
    }

    fun getAllValues(): List<T> {
        return realContainer.toList()
    }
}
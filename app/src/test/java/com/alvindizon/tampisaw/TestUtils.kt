package com.alvindizon.tampisaw

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.mockito.Mockito

fun <T> any(): T = Mockito.any()

// Taken here: https://stackoverflow.com/questions/63522656/what-is-the-correct-way-to-check-the-data-from-a-pagingdata-object-in-android-un
private val dcb = object : DifferCallback {
    override fun onChanged(position: Int, count: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
}

@ExperimentalCoroutinesApi
suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
    val items = mutableListOf<T>()
    val dif = object : PagingDataDiffer<T>(dcb, TestCoroutineDispatcher()) {
        override suspend fun presentNewList(previousList: NullPaddedList<T>, newList: NullPaddedList<T>, newCombinedLoadStates: CombinedLoadStates, lastAccessedIndex: Int): Int? {
            for (idx in 0 until newList.size)
                items.add(newList.getFromStorage(idx))
            return null
        }
    }
    dif.collectFrom(this)
    return items
}

open class TestObserver<T> : Observer<T> {

    val observedValues = mutableListOf<T?>()

    override fun onChanged(value: T?) {
        observedValues.add(value)
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}
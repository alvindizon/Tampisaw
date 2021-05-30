package com.alvindizon.tampisaw.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.core.toUnsplashCollection
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.ui.collections.UnsplashCollection
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

// TODO think of a way to make a generic paging source
class CollectionPagingSource (private val unsplashApi: UnsplashApi)
    : RxPagingSource<Int, UnsplashCollection>(){

    override fun getRefreshKey(state: PagingState<Int, UnsplashCollection>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, UnsplashCollection>> {
        val id = params.key ?: Const.PAGE_NUM
        return unsplashApi.getCollections(id, Const.PAGE_SIZE)
        .subscribeOn(Schedulers.io())
            .map { response ->
                response.map { it.toUnsplashCollection() }
            }
            .map<LoadResult<Int, UnsplashCollection>> { item ->
                LoadResult.Page(
                    data = item,
                    prevKey = if (id == Const.PAGE_NUM) null else id - 1,
                    nextKey =  if (item.isEmpty()) null else id + 1
                )
            }
            .onErrorReturn { e -> LoadResult.Error(e) }
    }
}

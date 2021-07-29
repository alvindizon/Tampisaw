package com.alvindizon.tampisaw.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.core.toUnsplashCollection
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.features.collections.UnsplashCollection
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SearchCollectionsPagingSource (private val unsplashApi: UnsplashApi, private val query: String)
    : RxPagingSource<Int, UnsplashCollection>(){

    override fun getRefreshKey(state: PagingState<Int, UnsplashCollection>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, UnsplashCollection>> {
        val page = params.key ?: Const.PAGE_NUM
        return unsplashApi.searchCollections(query, page, Const.PAGE_SIZE)
        .subscribeOn(Schedulers.io())
            .map { response ->
                response.results.map { it.toUnsplashCollection() }
            }
            .map<LoadResult<Int, UnsplashCollection>> { item ->
                LoadResult.Page(
                    data = item,
                    prevKey = if (page == Const.PAGE_NUM) null else page - 1,
                    nextKey =  if (item.isEmpty()) null else page + 1
                )
            }
            .onErrorReturn { e -> LoadResult.Error(e) }
    }
}

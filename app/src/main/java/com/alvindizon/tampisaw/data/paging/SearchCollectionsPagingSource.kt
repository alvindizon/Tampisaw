package com.alvindizon.tampisaw.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.data.networking.model.getcollections.GetCollectionsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SearchCollectionsPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : RxPagingSource<Int, GetCollectionsResponse>() {

    override fun getRefreshKey(state: PagingState<Int, GetCollectionsResponse>): Int? =
        state.anchorPosition

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, GetCollectionsResponse>> {
        val page = params.key ?: Const.PAGE_NUM
        return unsplashApi.searchCollections(query, page, Const.PAGE_SIZE)
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, GetCollectionsResponse>> { item ->
                LoadResult.Page(
                    data = item.results,
                    prevKey = if (page == Const.PAGE_NUM) null else page - 1,
                    nextKey = if (item.results.isEmpty()) null else page + 1
                )
            }
            .onErrorReturn { e -> LoadResult.Error(e) }
    }
}

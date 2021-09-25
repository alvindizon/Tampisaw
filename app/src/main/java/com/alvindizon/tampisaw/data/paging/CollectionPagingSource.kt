package com.alvindizon.tampisaw.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.data.networking.model.getcollections.GetCollectionsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

// TODO think of a way to make a generic paging source
class CollectionPagingSource(private val unsplashApi: UnsplashApi) :
    RxPagingSource<Int, GetCollectionsResponse>() {

    override fun getRefreshKey(state: PagingState<Int, GetCollectionsResponse>) =
        state.anchorPosition

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, GetCollectionsResponse>> {
        val id = params.key ?: Const.PAGE_NUM
        return unsplashApi.getCollections(id, Const.PAGE_SIZE)
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, GetCollectionsResponse>> { item ->
                LoadResult.Page(
                    data = item,
                    prevKey = if (id == Const.PAGE_NUM) null else id - 1,
                    nextKey = if (item.isEmpty()) null else id + 1
                )
            }
            .onErrorReturn { e -> LoadResult.Error(e) }
    }
}

package com.alvindizon.tampisaw.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.alvindizon.tampisaw.api.UnsplashApi
import com.alvindizon.tampisaw.api.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.core.Const
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

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

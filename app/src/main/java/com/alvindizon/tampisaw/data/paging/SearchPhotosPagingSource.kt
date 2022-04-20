package com.alvindizon.tampisaw.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.alvindizon.tampisaw.api.UnsplashApi
import com.alvindizon.tampisaw.api.model.listphotos.ListPhotosResponse
import com.alvindizon.tampisaw.core.Const
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchPhotosPagingSource(private val unsplashApi: UnsplashApi, private val query: String) :
    RxPagingSource<Int, ListPhotosResponse>() {

    override fun getRefreshKey(state: PagingState<Int, ListPhotosResponse>): Int? =
        state.anchorPosition

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListPhotosResponse>> {
        val page = params.key ?: Const.PAGE_NUM
        return unsplashApi.searchPhotos(
            query, page, Const.PAGE_SIZE, "relevant", null,
            "low", null, null
        )
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, ListPhotosResponse>> { item ->
                LoadResult.Page(
                    data = item.results,
                    prevKey = if (page == Const.PAGE_NUM) null else page - 1,
                    nextKey = if (item.results.isEmpty()) null else page + 1
                )
            }
            .onErrorReturn { e -> LoadResult.Error(e) }
    }
}
